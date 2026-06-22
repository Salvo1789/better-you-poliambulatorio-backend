package com.betteryou.backend.service;

import com.betteryou.backend.model.ServiceItem;
import com.betteryou.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class ServiceCatalogService {

    private final ServiceRepository serviceRepository;
    private final Duration cacheTtl;
    private volatile List<ServiceItem> cachedServices = List.of();
    private volatile Instant cacheExpiresAt = Instant.EPOCH;

    public ServiceCatalogService(
            ServiceRepository serviceRepository,
            @Value("${app.catalog.cache-ttl-seconds:300}") long cacheTtlSeconds
    ) {
        this.serviceRepository = serviceRepository;
        this.cacheTtl = Duration.ofSeconds(cacheTtlSeconds);
    }

    public List<ServiceItem> getAllServices() throws Exception {
        Instant now = Instant.now();
        if (now.isBefore(cacheExpiresAt)) {
            return cachedServices;
        }

        synchronized (this) {
            now = Instant.now();
            if (now.isBefore(cacheExpiresAt)) {
                return cachedServices;
            }

            cachedServices = serviceRepository.findAll();
            cacheExpiresAt = now.plus(cacheTtl);
            return cachedServices;
        }
    }
}
