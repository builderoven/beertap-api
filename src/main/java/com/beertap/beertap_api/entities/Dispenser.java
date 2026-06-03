package com.beertap.beertap_api.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

import com.beertap.beertap_api.entities.enums.DispenserStatus;

@Entity
@Table(name = "dispensers")
public class Dispenser {

    @Id
    private UUID id;

    @Column(precision = 10, scale = 4)
    private BigDecimal flowVolume;

    private String name;

    @Enumerated(EnumType.STRING)
    private DispenserStatus status;

    public Dispenser() {
    }

    public Dispenser(UUID id, BigDecimal flowVolume, String name) {
        this.id = id;
        this.flowVolume = flowVolume;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getFlowVolume() {
        return flowVolume;
    }

    public void setFlowVolume(BigDecimal flowVolume) {
        this.flowVolume = flowVolume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DispenserStatus getStatus() {
        return status;
    }

    public void setStatus(DispenserStatus status) {
        this.status = status;
    }
}