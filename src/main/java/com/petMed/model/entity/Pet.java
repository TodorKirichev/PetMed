package com.petMed.model.entity;

import com.petMed.model.enums.PetSpecies;
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
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetSpecies species;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private Integer age;

    @ManyToOne(optional = false)
    private User owner;

    @OneToMany(mappedBy = "pet")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
}