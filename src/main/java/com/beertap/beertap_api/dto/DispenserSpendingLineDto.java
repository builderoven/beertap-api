package com.beertap.beertap_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DispenserSpendingLineDto {

    private UUID id;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal flowVolume;
    private BigDecimal totalSpent;

    public DispenserSpendingLineDto() {
    }

    public DispenserSpendingLineDto(UUID id, LocalDateTime openedAt, LocalDateTime closedAt,
            BigDecimal flowVolume, BigDecimal totalSpent) {
        this.id = id;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
        this.flowVolume = flowVolume;
        this.totalSpent = totalSpent;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public BigDecimal getFlowVolume() {
        return flowVolume;
    }

    public void setFlowVolume(BigDecimal flowVolume) {
        this.flowVolume = flowVolume;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
}