package com.beertap.beertap_api.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.beertap.beertap_api.dto.DispenserDto;
import com.beertap.beertap_api.entities.Dispenser;
import com.beertap.beertap_api.mappers.DispenserMapper;
import com.beertap.beertap_api.repositories.DispenserRepository;
import com.beertap.beertap_api.services.DispenserService;

@Service
public class DispenserServiceImpl implements DispenserService {

    private final DispenserRepository dispenserRepository;

    public DispenserServiceImpl(DispenserRepository dispenserRepository) {
        this.dispenserRepository = dispenserRepository;
    }

    @Override
    public List<DispenserDto> getAllDispensers() {
        return dispenserRepository.findAll().stream()
                .map(DispenserMapper::toDto)
                .toList();
    }

    @Override
    public DispenserDto createDispenser(DispenserDto request) {
        Dispenser dispenser = new Dispenser(UUID.randomUUID(), request.getFlowVolume(), request.getName());
        Dispenser saved = dispenserRepository.save(dispenser);
        return DispenserMapper.toDto(saved);
    }
}