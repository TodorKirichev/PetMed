package com.petMed.clinic.service;

import com.petMed.clinic.model.Clinic;
import com.petMed.web.dto.VetData;
import com.petMed.web.dto.VetRegisterRequest;

public interface ClinicService {

    Clinic createClinicFromRegisterRequest(VetRegisterRequest registerRequest);

    Clinic createClinicFromVetData(VetData vetData);
}
