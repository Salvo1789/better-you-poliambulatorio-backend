package com.betteryou.backend.controller;

import com.betteryou.backend.model.ServiceItem;
import com.betteryou.backend.service.ServiceCatalogService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceCatalogService serviceCatalogService;

    public ServiceController(ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceItem> getAll() throws Exception {
        return serviceCatalogService.getAllServices();
    }
}