package com.petMed.repository;

import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {
    Optional<MedicalRecord> findByAppointment(Appointment appointment);
}
