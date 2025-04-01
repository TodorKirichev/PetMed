package com.petMed.UnitTests.clinic.service;

import com.petMed.clinic.model.CityName;
import com.petMed.clinic.model.Clinic;
import com.petMed.clinic.repository.ClinicRepository;
import com.petMed.clinic.service.ClinicService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClinicServiceUTest {

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private ClinicService clinicService;

    @Test
    void createClinic_Success() {
        String clinicName = "PetMed";
        CityName city = CityName.PLOVDIV;
        String address = "Ivan Vazov";
        String site = "www.petmed.com";

        Clinic clinic = Clinic.builder()
                .name(clinicName)
                .city(city)
                .address(address)
                .site(site)
                .build();

        when(clinicRepository.save(any())).thenReturn(clinic);

        Clinic result = clinicService.createClinic(clinicName, city, address, site);

        verify(clinicRepository,times(1)).save(any());
        assertEquals(clinicName, result.getName());
        assertEquals(city, result.getCity());
        assertEquals(address, result.getAddress());
        assertEquals(site, result.getSite());
    }
}
