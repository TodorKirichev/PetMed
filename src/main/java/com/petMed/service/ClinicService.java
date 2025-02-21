package com.petMed.service;

import com.petMed.model.dto.VetRegisterRequest;
import com.petMed.model.entity.Clinic;
import com.petMed.repository.ClinicRepository;
import org.springframework.stereotype.Service;

@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    public Clinic createClinic(VetRegisterRequest vetRegisterRequest) {
        Clinic clinic = Clinic.builder()
                .name(vetRegisterRequest.getClinicName())
                .city(vetRegisterRequest.getCity())
                .address(vetRegisterRequest.getAddress())
                .build();
        return clinicRepository.save(clinic);
    }
}
