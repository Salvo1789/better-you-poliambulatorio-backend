package com.betteryou.backend.service;

import com.betteryou.backend.dto.DoctorAccountRequest;
import com.betteryou.backend.model.DoctorAccount;
import com.betteryou.backend.repository.DoctorAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorAccountService {

    private final DoctorAccountRepository doctorAccountRepository;
    private final PasswordService passwordService;

    public DoctorAccountService(DoctorAccountRepository doctorAccountRepository, PasswordService passwordService) {
        this.doctorAccountRepository = doctorAccountRepository;
        this.passwordService = passwordService;
    }

    public DoctorAccount createAccount(DoctorAccountRequest request) throws Exception {
        String email = request.getEmail().trim().toLowerCase();
        if (doctorAccountRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor account already exists");
        }

        String salt = passwordService.createSalt();
        DoctorAccount account = new DoctorAccount();
        account.setName(request.getName().trim());
        account.setEmail(email);
        account.setRole(request.getRole().trim());
        account.setPasswordSalt(salt);
        account.setPasswordHash(passwordService.hashPassword(request.getPassword(), salt));
        account.setActive(true);
        account.setCreatedAt(Instant.now().toString());

        return doctorAccountRepository.save(account);
    }

    public List<DoctorAccount> getAccounts() throws Exception {
        return doctorAccountRepository.findAll();
    }

    public Optional<DoctorAccount> authenticate(String email, String password) throws Exception {
        Optional<DoctorAccount> account = doctorAccountRepository.findByEmail(email.trim().toLowerCase());
        if (account.isEmpty() || !account.get().isActive()) {
            return Optional.empty();
        }

        DoctorAccount doctorAccount = account.get();
        boolean matches = passwordService.matches(
                password,
                doctorAccount.getPasswordSalt(),
                doctorAccount.getPasswordHash()
        );

        return matches ? account : Optional.empty();
    }
}
