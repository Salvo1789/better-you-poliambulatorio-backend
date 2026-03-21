package com.betteryou.backend.service;

import com.betteryou.backend.model.ServiceItem;
import com.betteryou.backend.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCatalogService {

    private final ServiceRepository serviceRepository;

    public ServiceCatalogService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<ServiceItem> getAllServices() throws Exception {
        return serviceRepository.findAll();
    }
}