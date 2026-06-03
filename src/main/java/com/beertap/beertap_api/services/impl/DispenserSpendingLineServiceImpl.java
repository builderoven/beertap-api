package com.beertap.beertap_api.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beertap.beertap_api.dto.requests.UpdateStatusRequest;
import com.beertap.beertap_api.dto.responses.DispenserSpendingResponse;
import com.beertap.beertap_api.constants.DispenserConstants;
import com.beertap.beertap_api.dto.DispenserSpendingLineDto;
import com.beertap.beertap_api.entities.Dispenser;
import com.beertap.beertap_api.entities.DispenserSpendingLine;
import com.beertap.beertap_api.entities.enums.DispenserStatus;
import com.beertap.beertap_api.mappers.DispenserSpendingLineMapper;
import com.beertap.beertap_api.repositories.DispenserRepository;
import com.beertap.beertap_api.repositories.DispenserSpendingLineRepository;
import com.beertap.beertap_api.services.DispenserSpendingLineService;

@Service
public class DispenserSpendingLineServiceImpl implements DispenserSpendingLineService {

    private static final BigDecimal PRICE_PER_LITRE = new BigDecimal("12.25");

    private final DispenserSpendingLineRepository usageRepository;
    private final DispenserRepository dispenserRepository;

    public DispenserSpendingLineServiceImpl(DispenserSpendingLineRepository usageRepository,
            DispenserRepository dispenserRepository) {
        this.usageRepository = usageRepository;
        this.dispenserRepository = dispenserRepository;
    }

    @Override
    public void updateStatus(UUID dispenserId, UpdateStatusRequest request) {
        Dispenser dispenser = dispenserRepository.findById(dispenserId).orElseThrow();
        validateStatusChange(dispenser, request.getStatus());

        if (DispenserConstants.STATUS_OPEN.equals(request.getStatus()))
            openDispenser(dispenser, request.getUpdatedAt());
        else if (DispenserConstants.STATUS_CLOSE.equals(request.getStatus()))
            closeDispenser(dispenser, request.getUpdatedAt());
    }

    @Override
    public DispenserSpendingResponse getSpending(UUID dispenserId) {
        List<DispenserSpendingLine> usages = usageRepository.findByDispenserIdOrderByOpenedAtAsc(dispenserId);

        List<DispenserSpendingLineDto> usageDtos = usages.stream()
                .map(u -> {
                    DispenserSpendingLineDto dto = DispenserSpendingLineMapper.toDto(u);
                    if (dto.getClosedAt() == null) {
                        long seconds = Duration.between(u.getOpenedAt(), LocalDateTime.now()).getSeconds();
                        BigDecimal totalSpent = u.getFlowVolume()
                                .multiply(BigDecimal.valueOf(seconds))
                                .multiply(PRICE_PER_LITRE)
                                .setScale(2, RoundingMode.HALF_UP);
                        dto.setTotalSpent(totalSpent);
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        BigDecimal amount = usageDtos.stream()
                .map(DispenserSpendingLineDto::getTotalSpent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DispenserSpendingResponse response = new DispenserSpendingResponse();
        response.setAmount(amount);
        response.setUsages(usageDtos);
        return response;
    }

    private void validateStatusChange(Dispenser dispenser, String newStatus) {
        if ("open".equals(newStatus) && dispenser.getStatus() == DispenserStatus.OPEN)
            throw new IllegalStateException("Dispenser is already opened");
        if ("close".equals(newStatus) && dispenser.getStatus() != DispenserStatus.OPEN)
            throw new IllegalStateException("Dispenser is already closed");
    }

    private void openDispenser(Dispenser dispenser, LocalDateTime openedAt) {
        dispenser.setStatus(DispenserStatus.OPEN);
        dispenserRepository.save(dispenser);

        DispenserSpendingLine usage = new DispenserSpendingLine();
        usage.setDispenser(dispenser);
        usage.setOpenedAt(openedAt);
        usage.setFlowVolume(dispenser.getFlowVolume());
        usageRepository.save(usage);
    }

    private void closeDispenser(Dispenser dispenser, LocalDateTime closedAt) {
        dispenser.setStatus(DispenserStatus.CLOSED);
        dispenserRepository.save(dispenser);

        List<DispenserSpendingLine> usages = usageRepository.findByDispenserIdOrderByOpenedAtAsc(dispenser.getId());
        DispenserSpendingLine lastUsage = usages.get(usages.size() - 1);
        lastUsage.setClosedAt(closedAt);

        long seconds = Duration.between(lastUsage.getOpenedAt(), closedAt).getSeconds();
        BigDecimal totalSpent = lastUsage.getFlowVolume()
                .multiply(BigDecimal.valueOf(seconds))
                .multiply(PRICE_PER_LITRE)
                .setScale(3, RoundingMode.HALF_UP);
        lastUsage.setTotalSpent(totalSpent);

        usageRepository.save(lastUsage);
    }
}