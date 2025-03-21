package com.petMed.web;

import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.web.dto.MedicalRecordData;
import com.petMed.appointment.model.Appointment;
import com.petMed.pet.model.Pet;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.medicalRecord.service.MedicalRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> addRecord(@RequestBody MedicalRecordData medicalRecordData) {
        Appointment appointment = appointmentService.findById(medicalRecordData.getAppointmentId());
        Pet pet = appointment.getPet();

        MedicalRecord medicalRecord = medicalRecordService.createMedicalRecord(appointment, pet, medicalRecordData);
        appointmentService.changeStatusToCompleted(appointment, medicalRecord);

        return ResponseEntity.ok("Record created");
    }
}
