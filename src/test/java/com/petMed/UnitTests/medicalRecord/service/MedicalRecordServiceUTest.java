package com.petMed.UnitTests.medicalRecord.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.medicalRecord.repository.MedicalRecordRepository;
import com.petMed.medicalRecord.service.MedicalRecordService;
import com.petMed.pet.model.Pet;
import com.petMed.web.dto.MedicalRecordData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceUTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @Test
    void createMedicalRecord_Success() {
        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID());

        Pet pet = new Pet();

        MedicalRecordData medicalRecordData = new MedicalRecordData();
        medicalRecordData.setDiagnosis("diagnosis");
        medicalRecordData.setTreatment("treatment");
        medicalRecordData.setAppointmentId(appointment.getId());

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .diagnosis(medicalRecordData.getDiagnosis())
                .treatment(medicalRecordData.getTreatment())
                .createdOn(LocalDate.now())
                .appointment(appointment)
                .pet(pet)
                .build();

        when(medicalRecordRepository.save(any())).thenReturn(medicalRecord);

        MedicalRecord result = medicalRecordService.createMedicalRecord(appointment, pet, medicalRecordData);

        assertEquals(appointment.getId(), result.getAppointment().getId());
        assertEquals(medicalRecordData.getDiagnosis(), result.getDiagnosis());
        assertEquals(medicalRecordData.getTreatment(), result.getTreatment());
        verify(medicalRecordRepository, times(1)).save(any());
    }
}
