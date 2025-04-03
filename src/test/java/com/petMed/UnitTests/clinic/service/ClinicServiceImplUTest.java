package com.petMed.UnitTests.clinic.service;

import com.petMed.clinic.model.CityName;
import com.petMed.clinic.model.Clinic;
import com.petMed.clinic.repository.ClinicRepository;
import com.petMed.clinic.service.ClinicService;
import com.petMed.clinic.service.impl.ClinicServiceImpl;
import com.petMed.web.dto.VetRegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClinicServiceImplUTest {

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    void createClinic_Success() {
        VetRegisterRequest vetRegisterRequest = new VetRegisterRequest();
        vetRegisterRequest.setClinicName("PetMed");
        vetRegisterRequest.setCity(CityName.PLOVDIV);
        vetRegisterRequest.setAddress("address");
        vetRegisterRequest.setSite("site");

        Clinic clinic = Clinic.builder()
                .name("PetMed")
                .city(CityName.PLOVDIV)
                .address("address")
                .site("site")
                .build();

        when(clinicRepository.save(any())).thenReturn(clinic);

        Clinic result = clinicService.createClinicFromRegisterRequest(vetRegisterRequest);

        verify(clinicRepository,times(1)).save(any());
        assertEquals("PetMed", result.getName());
        assertEquals(CityName.PLOVDIV, result.getCity());
        assertEquals("address", result.getAddress());
        assertEquals("site", result.getSite());
    }
}
