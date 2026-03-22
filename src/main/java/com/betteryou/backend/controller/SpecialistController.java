package com.betteryou.backend.controller;

import com.betteryou.backend.model.Specialist;
import com.betteryou.backend.service.SpecialistService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/specialists")
public class SpecialistController {

    private final SpecialistService specialistService;

    public SpecialistController(SpecialistService specialistService) {
        this.specialistService = specialistService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Specialist> getAll() throws Exception {
        return specialistService.getAllSpecialists();
    }
}