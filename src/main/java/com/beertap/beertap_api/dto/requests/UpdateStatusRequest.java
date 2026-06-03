package com.beertap.beertap_api.dto.requests;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateStatusRequest {

    private String status;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public UpdateStatusRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}