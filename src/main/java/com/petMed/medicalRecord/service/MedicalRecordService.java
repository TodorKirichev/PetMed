package com.petMed.medicalRecord.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.pet.model.Pet;
import com.petMed.medicalRecord.repository.MedicalRecordRepository;
import com.petMed.web.dto.MedicalRecordData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord createMedicalRecord(Appointment appointment, Pet pet, MedicalRecordData medicalRecordData) {
        MedicalRecord medicalRecord = createRecord(appointment, pet, medicalRecordData);
        return medicalRecordRepository.save(medicalRecord);
    }

    private static MedicalRecord createRecord(Appointment appointment, Pet pet, MedicalRecordData medicalRecordData) {
        return MedicalRecord.builder()
                .diagnosis(medicalRecordData.getDiagnosis())
                .treatment(medicalRecordData.getTreatment())
                .createdOn(LocalDate.now())
                .appointment(appointment)
                .pet(pet)
                .build();
    }
}
