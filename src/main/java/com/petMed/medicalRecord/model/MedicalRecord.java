package com.petMed.medicalRecord.model;

import com.petMed.appointment.model.Appointment;
import com.petMed.pet.model.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String diagnosis;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String treatment;

    @Column(nullable = false)
    private LocalDate createdOn;

    @OneToOne(mappedBy = "medicalRecord")
    private Appointment appointment;

    @ManyToOne
    private Pet pet;
}
