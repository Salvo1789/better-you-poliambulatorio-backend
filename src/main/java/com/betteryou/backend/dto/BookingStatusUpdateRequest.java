package com.betteryou.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BookingStatusUpdateRequest {

    @NotBlank
    @Pattern(regexp = "NEW|CONTACTED|CONFIRMED|CANCELLED")
    private String status;

    public BookingStatusUpdateRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
