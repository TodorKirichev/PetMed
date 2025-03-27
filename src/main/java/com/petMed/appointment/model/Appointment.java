package com.petMed.appointment.model;

import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.pet.model.Pet;
import com.petMed.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @ManyToOne
    private Pet pet;

    @OneToOne
    private MedicalRecord medicalRecord;

    @ManyToOne(optional = false)
    private User vet;

}