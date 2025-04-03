package com.petMed.medicalRecord.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.pet.model.Pet;
import com.petMed.web.dto.MedicalRecordData;

public interface MedicalRecordService {

    MedicalRecord createMedicalRecord(Appointment appointment, Pet pet, MedicalRecordData medicalRecordData);

}
