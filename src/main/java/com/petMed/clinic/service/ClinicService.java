package com.petMed.clinic.service;

import com.petMed.web.dto.VetRegisterRequest;
import com.petMed.clinic.model.Clinic;
import com.petMed.clinic.repository.ClinicRepository;
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
