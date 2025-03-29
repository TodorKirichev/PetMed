package com.petMed.util;

import com.petMed.clinic.model.CityName;
import com.petMed.clinic.model.Clinic;
import com.petMed.clinic.repository.ClinicRepository;
import com.petMed.pet.model.Pet;
import com.petMed.pet.model.PetSpecies;
import com.petMed.pet.repository.PetRepository;
import com.petMed.scheduler.AppointmentScheduler;
import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import com.petMed.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UsersLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClinicRepository clinicRepository;
    private final AppointmentScheduler appointmentScheduler;
    private final PetRepository petRepository;

    public UsersLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, ClinicRepository clinicRepository, AppointmentScheduler appointmentScheduler, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clinicRepository = clinicRepository;
        this.appointmentScheduler = appointmentScheduler;
        this.petRepository = petRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            createAdmin();
            createUser();
            createVets();
        }
    }

    private void createAdmin() {
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .role(Role.ADMIN)
                .firstName("Admin")
                .lastName("Adminov")
                .phone("0898000000")
                .email("admin@petmed.com")
                .isActive(true)
                .build();
        userRepository.save(admin);
    }

    private void createVets() {
        for (int i = 1; i < 11; i++) {
            Clinic clinic = createClinic(i);
            createVet(i, clinic);
        }
    }

    private Clinic createClinic(int i) {
        Clinic clinic = Clinic.builder()
                .name("Pet med " + i)
                .city(CityName.PLOVDIV)
                .address("Ivan Vazov " + i)
                .site("https://pet-med" + i + ".com")
                .build();
        clinicRepository.save(clinic);
        return clinic;
    }

    private void createVet(int i, Clinic clinic) {
        User vet = User.builder()
                .username("vet" + i)
                .password(passwordEncoder.encode("vet" + i))
                .role(Role.VET)
                .firstName("Vet" + i)
                .lastName("Vet" + i)
                .phone("089800001" + i)
                .email("vet" + i + "@petmed.com")
                .imageUrl("http://res.cloudinary.com/dj4dqvael/image/upload/v1743077520/ay5kzqhnirzqwv3gmtmj.jpg")
                .clinic(clinic)
                .isActive(true)
                .build();
        userRepository.save(vet);
        appointmentScheduler.generateAppointmentsForVetOnRegistration(vet);
    }

    private void createUser() {
        User owner = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .role(Role.PET_OWNER)
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("0898000020")
                .email("user@petmed.com")
                .isActive(true)
                .build();
        userRepository.save(owner);

        addPets(owner);
    }

    private void addPets(User owner) {
        for (int i = 1; i < 6; i++) {
            Pet pet = Pet.builder()
                    .owner(owner)
                    .name("Timo" + i)
                    .species(PetSpecies.CAT)
                    .breed("Scottish Fold")
                    .dateOfBirth(LocalDate.of(2021, 9, 13))
                    .imageUrl("http://res.cloudinary.com/dj4dqvael/image/upload/v1743072396/lfph1zniv1f6vxs7alwg.jpg")
                    .build();
            petRepository.save(pet);
        }
    }
}
