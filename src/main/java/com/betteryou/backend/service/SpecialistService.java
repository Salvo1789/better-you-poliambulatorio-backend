
package com.betteryou.backend.service;

import com.betteryou.backend.model.Specialist;
import com.betteryou.backend.repository.SpecialistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialistService {

    private final SpecialistRepository specialistRepository;

    public SpecialistService(SpecialistRepository specialistRepository) {
        this.specialistRepository = specialistRepository;
    }

    public List<Specialist> getAllSpecialists() throws Exception {
        return specialistRepository.findAll();
    }
}