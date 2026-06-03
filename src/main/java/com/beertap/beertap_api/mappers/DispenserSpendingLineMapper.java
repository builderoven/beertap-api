package com.beertap.beertap_api.mappers;

import com.beertap.beertap_api.dto.DispenserSpendingLineDto;
import com.beertap.beertap_api.entities.DispenserSpendingLine;

public class DispenserSpendingLineMapper {

    public static DispenserSpendingLineDto toDto(DispenserSpendingLine usage) {
        return new DispenserSpendingLineDto(usage.getId(),
                usage.getOpenedAt(),
                usage.getClosedAt(),
                usage.getFlowVolume(),
                usage.getTotalSpent());
    }
}