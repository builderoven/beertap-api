package com.beertap.beertap_api.services;

import java.util.List;

import com.beertap.beertap_api.dto.DispenserDto;

public interface DispenserService {
    DispenserDto createDispenser(DispenserDto request);
    List<DispenserDto> getAllDispensers();
}