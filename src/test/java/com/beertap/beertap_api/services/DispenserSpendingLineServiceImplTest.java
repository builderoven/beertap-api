package com.beertap.beertap_api.services;

import com.beertap.beertap_api.dto.requests.UpdateStatusRequest;
import com.beertap.beertap_api.dto.responses.DispenserSpendingResponse;
import com.beertap.beertap_api.dto.DispenserSpendingLineDto;
import com.beertap.beertap_api.entities.Dispenser;
import com.beertap.beertap_api.entities.DispenserSpendingLine;
import com.beertap.beertap_api.entities.enums.DispenserStatus;
import com.beertap.beertap_api.repositories.DispenserRepository;
import com.beertap.beertap_api.repositories.DispenserSpendingLineRepository;
import com.beertap.beertap_api.services.impl.DispenserSpendingLineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispenserSpendingLineServiceImplTest {

    @Mock
    private DispenserSpendingLineRepository usageRepository;
    @Mock
    private DispenserRepository dispenserRepository;
    @InjectMocks
    private DispenserSpendingLineServiceImpl service;

    @Captor
    private ArgumentCaptor<DispenserSpendingLine> usageCaptor;

    private UUID dispenserId;
    private Dispenser dispenser;

    @BeforeEach
    void setUp() {
        dispenserId = UUID.randomUUID();
        dispenser = new Dispenser(dispenserId, new BigDecimal("0.075"), null);
    }

    @Test
    void openDispenser_shouldSetStatusOpenAndCreateUsage() {
        when(dispenserRepository.findById(dispenserId)).thenReturn(Optional.of(dispenser));

        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus("open");
        request.setUpdatedAt(LocalDateTime.of(2025, 5, 28, 10, 0, 0));

        service.updateStatus(dispenserId, request);

        assertEquals(DispenserStatus.OPEN, dispenser.getStatus());
        verify(dispenserRepository).save(dispenser);
        verify(usageRepository).save(usageCaptor.capture());
        assertEquals(dispenser, usageCaptor.getValue().getDispenser());
        assertEquals(request.getUpdatedAt(), usageCaptor.getValue().getOpenedAt());
    }

    @Test
    void openDispenser_whenAlreadyOpen_shouldThrowConflict() {
        dispenser.setStatus(DispenserStatus.OPEN);
        when(dispenserRepository.findById(dispenserId)).thenReturn(Optional.of(dispenser));

        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus("open");

        assertThrows(IllegalStateException.class, () -> service.updateStatus(dispenserId, request));
        verify(usageRepository, never()).save(any());
    }

    @Test
    void closeDispenser_shouldCalculateTotalSpent() {
        dispenser.setStatus(DispenserStatus.OPEN);
        when(dispenserRepository.findById(dispenserId)).thenReturn(Optional.of(dispenser));

        DispenserSpendingLine usage = new DispenserSpendingLine();
        usage.setDispenser(dispenser);
        usage.setOpenedAt(LocalDateTime.of(2025, 5, 28, 10, 0, 0));
        usage.setFlowVolume(new BigDecimal("0.075"));
        when(usageRepository.findByDispenserIdOrderByOpenedAtAsc(dispenserId))
                .thenReturn(List.of(usage));

        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus("close");
        request.setUpdatedAt(LocalDateTime.of(2025, 5, 28, 10, 1, 30));

        service.updateStatus(dispenserId, request);

        assertEquals(DispenserStatus.CLOSED, dispenser.getStatus());
        assertEquals(request.getUpdatedAt(), usage.getClosedAt());
        // 0.075 * 90 * 12.25 = 82.6875
        assertEquals(0, new BigDecimal("82.688").compareTo(usage.getTotalSpent()));
        verify(usageRepository).save(usage);
    }

    @Test
    void closeDispenser_whenAlreadyClosed_shouldThrowConflict() {
        dispenser.setStatus(DispenserStatus.CLOSED);
        when(dispenserRepository.findById(dispenserId)).thenReturn(Optional.of(dispenser));

        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus("close");

        assertThrows(IllegalStateException.class, () -> service.updateStatus(dispenserId, request));
        verify(usageRepository, never()).save(any());
    }

    @Test
    void getSpending_shouldReturnAmountAndUsages() {
        DispenserSpendingLine usage1 = new DispenserSpendingLine();
        usage1.setOpenedAt(LocalDateTime.of(2025, 5, 28, 10, 0, 0));
        usage1.setClosedAt(LocalDateTime.of(2025, 5, 28, 10, 0, 50));
        usage1.setFlowVolume(new BigDecimal("0.064"));
        usage1.setTotalSpent(new BigDecimal("39.200"));

        DispenserSpendingLine usage2 = new DispenserSpendingLine();
        usage2.setOpenedAt(LocalDateTime.of(2025, 5, 28, 10, 50, 58));
        usage2.setClosedAt(LocalDateTime.of(2025, 5, 28, 10, 51, 20));
        usage2.setFlowVolume(new BigDecimal("0.064"));
        usage2.setTotalSpent(new BigDecimal("17.248"));

        when(usageRepository.findByDispenserIdOrderByOpenedAtAsc(dispenserId))
                .thenReturn(List.of(usage1, usage2));

        DispenserSpendingResponse response = service.getSpending(dispenserId);

        assertEquals(0, new BigDecimal("56.448").compareTo(response.getAmount()));
        assertEquals(2, response.getUsages().size());
    }

    @Test
    void getSpending_withOpenUsage_shouldCalculatePartial() {
        DispenserSpendingLine usage = new DispenserSpendingLine();
        usage.setOpenedAt(LocalDateTime.now().minusSeconds(60));
        usage.setClosedAt(null);
        usage.setFlowVolume(new BigDecimal("0.075"));

        when(usageRepository.findByDispenserIdOrderByOpenedAtAsc(dispenserId))
                .thenReturn(List.of(usage));

        DispenserSpendingResponse response = service.getSpending(dispenserId);

        assertEquals(1, response.getUsages().size());
        DispenserSpendingLineDto dto = response.getUsages().get(0);
        assertNull(dto.getClosedAt());
        assertNotNull(dto.getTotalSpent());
        assertTrue(dto.getTotalSpent().compareTo(BigDecimal.ZERO) > 0);
    }
}