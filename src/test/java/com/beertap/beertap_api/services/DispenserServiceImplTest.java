package com.beertap.beertap_api.services;

import com.beertap.beertap_api.dto.DispenserDto;
import com.beertap.beertap_api.entities.Dispenser;
import com.beertap.beertap_api.repositories.DispenserRepository;
import com.beertap.beertap_api.services.impl.DispenserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispenserServiceImplTest {

    @Mock
    private DispenserRepository dispenserRepository;

    @InjectMocks
    private DispenserServiceImpl service;

    @Captor
    private ArgumentCaptor<Dispenser> dispenserCaptor;

    @Test
    void createDispenser_shouldSaveAndReturnDto() {
        DispenserDto request = new DispenserDto(null, new BigDecimal("0.064"), null, "Barra Central");

        Dispenser savedEntity = new Dispenser(UUID.randomUUID(), new BigDecimal("0.064"), "Barra Central");
        when(dispenserRepository.save(any())).thenReturn(savedEntity);

        DispenserDto result = service.createDispenser(request);

        assertNotNull(result.getId());
        assertEquals(0, new BigDecimal("0.064").compareTo(result.getFlowVolume()));
        assertEquals("Barra Central", result.getName());
        verify(dispenserRepository).save(any());
    }

    @Test
    void createDispenser_withNullName_shouldSaveWithoutName() {
        DispenserDto request = new DispenserDto(null, new BigDecimal("0.064"), null, null);

        Dispenser savedEntity = new Dispenser(UUID.randomUUID(), new BigDecimal("0.064"), null);
        when(dispenserRepository.save(any())).thenReturn(savedEntity);

        DispenserDto result = service.createDispenser(request);

        assertNull(result.getName());
        verify(dispenserRepository).save(any());
    }

    @Test
    void getAllDispensers_shouldReturnAll() {
        Dispenser d1 = new Dispenser(UUID.randomUUID(), new BigDecimal("0.064"), "Barra 1");
        Dispenser d2 = new Dispenser(UUID.randomUUID(), new BigDecimal("0.075"), "Barra 2");
        when(dispenserRepository.findAll()).thenReturn(List.of(d1, d2));

        List<DispenserDto> result = service.getAllDispensers();

        assertEquals(2, result.size());
        assertEquals("Barra 1", result.get(0).getName());
        assertEquals("Barra 2", result.get(1).getName());
        verify(dispenserRepository).findAll();
    }

    @Test
    void getAllDispensers_whenEmpty_shouldReturnEmptyList() {
        when(dispenserRepository.findAll()).thenReturn(List.of());

        List<DispenserDto> result = service.getAllDispensers();

        assertTrue(result.isEmpty());
        verify(dispenserRepository).findAll();
    }
}
