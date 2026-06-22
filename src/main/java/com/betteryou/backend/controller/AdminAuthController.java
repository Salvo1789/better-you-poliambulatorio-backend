package com.betteryou.backend.controller;

import com.betteryou.backend.dto.AdminLoginRequest;
import com.betteryou.backend.dto.AdminSessionResponse;
import com.betteryou.backend.service.AdminAuthService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AdminSessionResponse login(@Valid @RequestBody AdminLoginRequest request) throws Exception {
        return adminAuthService.login(request.getEmail(), request.getPassword());
    }
}
