package com.beertap.beertap_api.dto.responses;

import java.math.BigDecimal;
import java.util.List;
import com.beertap.beertap_api.dto.DispenserSpendingLineDto;

public class DispenserSpendingResponse {
    private BigDecimal amount;
    private List<DispenserSpendingLineDto> usages;

    public DispenserSpendingResponse() {}

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public List<DispenserSpendingLineDto> getUsages() { return usages; }
    public void setUsages(List<DispenserSpendingLineDto> usages) { this.usages = usages; }
}