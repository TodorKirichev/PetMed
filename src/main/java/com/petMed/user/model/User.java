package com.petMed.user.model;

import com.petMed.appointment.model.Appointment;
import com.petMed.clinic.model.Clinic;
import com.petMed.pet.model.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String password;

    @OneToOne
    private Clinic clinic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "owner")
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "vet")
    private List<Appointment> appointments = new ArrayList<>();

    @Column
    private boolean isActive;
}