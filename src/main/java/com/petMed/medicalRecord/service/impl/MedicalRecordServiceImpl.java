package com.petMed.medicalRecord.service.impl;

import com.petMed.appointment.model.Appointment;
import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.medicalRecord.service.MedicalRecordService;
import com.petMed.pet.model.Pet;
import com.petMed.medicalRecord.repository.MedicalRecordRepository;
import com.petMed.web.dto.MedicalRecordData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord createMedicalRecord(Appointment appointment, Pet pet, MedicalRecordData medicalRecordData) {
        MedicalRecord medicalRecord = createRecord(appointment, pet, medicalRecordData);
        return medicalRecordRepository.save(medicalRecord);
    }

    private MedicalRecord createRecord(Appointment appointment, Pet pet, MedicalRecordData medicalRecordData) {
        return MedicalRecord.builder()
                .diagnosis(medicalRecordData.getDiagnosis())
                .treatment(medicalRecordData.getTreatment())
                .createdOn(LocalDate.now())
                .appointment(appointment)
                .pet(pet)
                .build();
    }
}
