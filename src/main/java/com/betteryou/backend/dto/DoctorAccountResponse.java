package com.betteryou.backend.dto;

import com.betteryou.backend.model.DoctorAccount;

public class DoctorAccountResponse {

    private String id;
    private String name;
    private String email;
    private String role;
    private boolean active;
    private String createdAt;

    public DoctorAccountResponse(DoctorAccount account) {
        this.id = account.getId();
        this.name = account.getName();
        this.email = account.getEmail();
        this.role = account.getRole();
        this.active = account.isActive();
        this.createdAt = account.getCreatedAt();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
