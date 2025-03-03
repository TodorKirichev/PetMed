package com.petMed.medicalRecord.repository;

import com.petMed.appointment.model.Appointment;
import com.petMed.medicalRecord.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {
    Optional<MedicalRecord> findByAppointment(Appointment appointment);
}
