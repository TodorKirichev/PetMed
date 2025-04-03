package com.petMed.clinic.service.impl;

import com.petMed.clinic.model.Clinic;
import com.petMed.clinic.repository.ClinicRepository;
import com.petMed.clinic.service.ClinicService;
import com.petMed.web.dto.VetData;
import com.petMed.web.dto.VetRegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicServiceImpl(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    public Clinic createClinicFromRegisterRequest(VetRegisterRequest registerRequest) {
        Clinic clinic = Clinic.builder()
                .name(registerRequest.getClinicName())
                .city(registerRequest.getCity())
                .address(registerRequest.getAddress())
                .site(registerRequest.getSite())
                .build();
        return clinicRepository.save(clinic);
    }

    public Clinic createClinicFromVetData(VetData vetData) {
        Clinic clinic = Clinic.builder()
                .name(vetData.getClinicName())
                .city(vetData.getCity())
                .address(vetData.getAddress())
                .site(vetData.getSite())
                .build();
        return clinicRepository.save(clinic);
    }
}
