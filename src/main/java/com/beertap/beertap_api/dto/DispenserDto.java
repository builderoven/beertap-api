package com.beertap.beertap_api.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DispenserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    private BigDecimal flowVolume;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    public DispenserDto() {
    }

    public DispenserDto(UUID id, BigDecimal flowVolume, String status, String name) {
        this.id = id;
        this.flowVolume = flowVolume;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}