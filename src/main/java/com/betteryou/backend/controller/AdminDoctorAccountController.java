package com.betteryou.backend.controller;

import com.betteryou.backend.dto.DoctorAccountRequest;
import com.betteryou.backend.dto.DoctorAccountResponse;
import com.betteryou.backend.service.AdminAuthService;
import com.betteryou.backend.service.DoctorAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/doctors")
public class AdminDoctorAccountController {

    private final DoctorAccountService doctorAccountService;
    private final AdminAuthService adminAuthService;

    public AdminDoctorAccountController(DoctorAccountService doctorAccountService, AdminAuthService adminAuthService) {
        this.doctorAccountService = doctorAccountService;
        this.adminAuthService = adminAuthService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DoctorAccountResponse> getAll(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        adminAuthService.requireAdmin(authorizationHeader);
        return doctorAccountService.getAccounts().stream()
                .map(DoctorAccountResponse::new)
                .toList();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorAccountResponse create(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody DoctorAccountRequest request
    ) throws Exception {
        adminAuthService.requireAdmin(authorizationHeader);
        return new DoctorAccountResponse(doctorAccountService.createAccount(request));
    }
}
