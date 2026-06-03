package com.beertap.beertap_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beertap.beertap_api.dto.DispenserDto;
import com.beertap.beertap_api.dto.requests.UpdateStatusRequest;
import com.beertap.beertap_api.dto.responses.DispenserSpendingResponse;
import com.beertap.beertap_api.services.DispenserService;
import com.beertap.beertap_api.services.DispenserSpendingLineService;

@RestController
@RequestMapping("/dispenser")
public class DispenserController {

    private final DispenserService dispenserService;
    private final DispenserSpendingLineService usageService;

    public DispenserController(DispenserService dispenserService,
                               DispenserSpendingLineService usageService) {
        this.dispenserService = dispenserService;
        this.usageService = usageService;
    }

    @GetMapping("/dispensers")
    public ResponseEntity<List<DispenserDto>> getAllDispensers() {
        List<DispenserDto> dispensers = dispenserService.getAllDispensers();
        return ResponseEntity.ok(dispensers);
    }

    @PostMapping
    public ResponseEntity<DispenserDto> createDispenser(@RequestBody DispenserDto request) {
        DispenserDto created = dispenserService.createDispenser(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID id,
                                             @RequestBody UpdateStatusRequest request) {
        usageService.updateStatus(id, request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}/spending")
    public ResponseEntity<DispenserSpendingResponse> getSpending(@PathVariable UUID id) {
        DispenserSpendingResponse spending = usageService.getSpending(id);
        return ResponseEntity.ok(spending);
    }
}