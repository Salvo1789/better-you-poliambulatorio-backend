package com.betteryou.backend.service;

import com.betteryou.backend.dto.AdminSessionResponse;
import com.betteryou.backend.model.DoctorAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdminAuthService {

    private final String bootstrapEmail;
    private final String bootstrapPassword;
    private final Duration tokenTtl;
    private final DoctorAccountService doctorAccountService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, AdminSession> activeTokens = new ConcurrentHashMap<>();

    public AdminAuthService(
            @Value("${app.admin.email:}") String bootstrapEmail,
            @Value("${app.admin.password:}") String bootstrapPassword,
            @Value("${app.admin.token-ttl-minutes:480}") long tokenTtlMinutes,
            DoctorAccountService doctorAccountService
    ) {
        this.bootstrapEmail = bootstrapEmail;
        this.bootstrapPassword = bootstrapPassword;
        this.tokenTtl = Duration.ofMinutes(tokenTtlMinutes);
        this.doctorAccountService = doctorAccountService;
    }

    public AdminSessionResponse login(String email, String password) throws Exception {
        AdminSession session = doctorAccountService.authenticate(email, password)
                .map(this::toSession)
                .orElseGet(() -> authenticateBootstrapAdmin(email, password));

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = createToken();
        activeTokens.put(token, session.withToken(token, Instant.now().plus(tokenTtl)));
        return new AdminSessionResponse(token, session.name(), session.email(), session.role());
    }

    public AdminSession requireValidToken(String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        AdminSession session = activeTokens.get(token);

        if (session == null || session.expiresAt().isBefore(Instant.now())) {
            activeTokens.remove(token);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired admin session");
        }

        return session;
    }

    public void requireAdmin(String authorizationHeader) {
        AdminSession session = requireValidToken(authorizationHeader);
        if (!"ADMIN".equals(session.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin role required");
        }
    }

    private AdminSession toSession(DoctorAccount account) {
        return new AdminSession(null, account.getName(), account.getEmail(), account.getRole(), null);
    }

    private AdminSession authenticateBootstrapAdmin(String email, String password) {
        if (bootstrapEmail.isBlank() || bootstrapPassword.isBlank()
                || !bootstrapEmail.equalsIgnoreCase(email.trim())
                || !bootstrapPassword.equals(password)) {
            return null;
        }

        return new AdminSession(null, "Amministratore", bootstrapEmail, "ADMIN", null);
    }

    private String createToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing admin session");
        }

        return authorizationHeader.substring("Bearer ".length()).trim();
    }

    public record AdminSession(String token, String name, String email, String role, Instant expiresAt) {
        AdminSession withToken(String token, Instant expiresAt) {
            return new AdminSession(token, name, email, role, expiresAt);
        }
    }
}
