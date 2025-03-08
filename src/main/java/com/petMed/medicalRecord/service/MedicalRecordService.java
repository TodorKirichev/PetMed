package com.petMed.medicalRecord.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.pet.model.Pet;
import com.petMed.medicalRecord.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public void createMedicalRecord(Appointment appointment, Pet pet, String diagnosis, String treatment) {
        Optional<MedicalRecord> byAppointment = medicalRecordRepository.findByAppointment(appointment);
        if (byAppointment.isPresent()) {
            throw new RuntimeException("Already added medical record");
        }
        MedicalRecord medicalRecord = createRecord(appointment, pet, diagnosis, treatment);

        medicalRecordRepository.save(medicalRecord);
    }

    private static MedicalRecord createRecord(Appointment appointment, Pet pet, String diagnosis, String treatment) {
        return MedicalRecord.builder()
                .diagnosis(diagnosis)
                .treatment(treatment)
                .createdOn(LocalDate.now())
                .appointment(appointment)
                .pet(pet)
                .build();
    }
}
