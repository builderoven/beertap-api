package com.beertap.beertap_api.mappers;

import com.beertap.beertap_api.dto.DispenserDto;
import com.beertap.beertap_api.entities.Dispenser;

public class DispenserMapper {

    public static DispenserDto toDto(Dispenser dispenser) {
        return new DispenserDto(dispenser.getId(),
                dispenser.getFlowVolume(),
                dispenser.getStatus() != null ? dispenser.getStatus().name() : null,
                dispenser.getName());
    }
}