package com.petMed.controller;

import com.petMed.model.dto.MedicalRecordDto;
import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.MedicalRecord;
import com.petMed.model.entity.Pet;
import com.petMed.service.AppointmentService;
import com.petMed.service.MedicalRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordControllerREST {

    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordControllerREST(AppointmentService appointmentService, MedicalRecordService medicalRecordService) {
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<String> addRecord(@RequestBody MedicalRecordDto medicalRecordDto) {
        Appointment appointment = appointmentService.findById(UUID.fromString(medicalRecordDto.getAppointmentId()));
        Pet pet = appointment.getPet();
        String diagnosis = medicalRecordDto.getDiagnosis();
        String treatment = medicalRecordDto.getTreatment();

        medicalRecordService.createRecord(appointment, pet, diagnosis, treatment);

        return ResponseEntity.ok("Record created");
    }
}
