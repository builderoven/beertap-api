package com.beertap.beertap_api.services;

import java.util.UUID;
import com.beertap.beertap_api.dto.requests.UpdateStatusRequest;
import com.beertap.beertap_api.dto.responses.DispenserSpendingResponse;

public interface DispenserSpendingLineService {
    void updateStatus(UUID dispenserId, UpdateStatusRequest request);
    DispenserSpendingResponse getSpending(UUID dispenserId);
}