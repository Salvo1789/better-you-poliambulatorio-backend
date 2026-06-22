
package com.betteryou.backend.service;

import com.betteryou.backend.model.Specialist;
import com.betteryou.backend.repository.SpecialistRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class SpecialistService {

    private final SpecialistRepository specialistRepository;
    private final Duration cacheTtl;
    private volatile List<Specialist> cachedSpecialists = List.of();
    private volatile Instant cacheExpiresAt = Instant.EPOCH;

    public SpecialistService(
            SpecialistRepository specialistRepository,
            @Value("${app.catalog.cache-ttl-seconds:300}") long cacheTtlSeconds
    ) {
        this.specialistRepository = specialistRepository;
        this.cacheTtl = Duration.ofSeconds(cacheTtlSeconds);
    }

    public List<Specialist> getAllSpecialists() throws Exception {
        Instant now = Instant.now();
        if (now.isBefore(cacheExpiresAt)) {
            return cachedSpecialists;
        }

        synchronized (this) {
            now = Instant.now();
            if (now.isBefore(cacheExpiresAt)) {
                return cachedSpecialists;
            }

            cachedSpecialists = specialistRepository.findAll();
            cacheExpiresAt = now.plus(cacheTtl);
            return cachedSpecialists;
        }
    }
}
