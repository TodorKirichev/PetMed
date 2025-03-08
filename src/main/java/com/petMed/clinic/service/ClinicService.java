package com.petMed.clinic.service;

import com.petMed.clinic.model.CityName;
import com.petMed.clinic.model.Clinic;
import com.petMed.clinic.repository.ClinicRepository;
import org.springframework.stereotype.Service;

@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    public Clinic createClinic(String clinicName, CityName city, String address, String site) {
//        Optional<Clinic> byName = clinicRepository.findByName(vetRegisterRequest.getClinicName());
//        if (byName.isPresent()) {
//            Clinic clinic = byName.get();
//            clinic.setCity(vetRegisterRequest.getCity());
//            clinic.setAddress(vetRegisterRequest.getAddress());
//            clinic.setSite(vetRegisterRequest.getSite());
//            return clinicRepository.save(clinic);
//        }
        Clinic clinic = Clinic.builder()
                .name(clinicName)
                .city(city)
                .address(address)
                .site(site)
                .build();
        return clinicRepository.save(clinic);
    }
}
