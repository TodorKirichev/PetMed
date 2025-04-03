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
import java.util.List;

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
                .password(passwordEncoder.encode("123"))
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
        List<String> images = List.of("http://res.cloudinary.com/dj4dqvael/image/upload/v1743665872/e8tnzl7li7jmxfksowe4.jpg",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743665935/iwgcqofm3rjl7w9zzmb5.jpg",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743666032/hess1dxwvwkaunlgrjdq.avif",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743665984/ms0he7bnrejfasmjifg5.webp",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743665895/mvzcsu5ia8f5rqizchzh.jpg",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743665882/mvfkeedjvbrof6amgxlg.avif",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743665793/lroyfefvfthnwpccyrlo.jpg",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743665779/h2q6og8anltbskmnzxjd.webp",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743665805/qam9cbkdlzyvhzwme4fi.avif",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743665907/sqmezxc8entdpbywxkzw.jpg");
        User vet = User.builder()
                .username("vet" + i)
                .password(passwordEncoder.encode("123"))
                .role(Role.VET)
                .firstName("Vet" + i)
                .lastName("Vet" + i)
                .phone("089800001" + i)
                .email("vet" + i + "@petmed.com")
                .imageUrl(images.get(i - 1))
                .clinic(clinic)
                .isActive(true)
                .build();
        userRepository.save(vet);
        appointmentScheduler.generateAppointmentsForVetOnRegistration(vet);
    }

    private void createUser() {
        User owner = User.builder()
                .username("user")
                .password(passwordEncoder.encode("123"))
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
        List<String> images = List.of("http://res.cloudinary.com/dj4dqvael/image/upload/v1743667762/hayr1fozsnwelfosjacd.jpg",
                "http://res.cloudinary.com/dj4dqvael/image/upload/v1743667416/atmwjvyvogpyy9c8xlnk.webp",
                "https://res.cloudinary.com/dj4dqvael/image/upload/v1743072396/lfph1zniv1f6vxs7alwg.jpg");
        List<PetSpecies> species = List.of(PetSpecies.PARROT, PetSpecies.DOG, PetSpecies.CAT);
        List<String> breeds = List.of("Macaw", "Golden retriever", "Scottish fold");
        for (int i = 1; i < 4; i++) {
            Pet pet = Pet.builder()
                    .owner(owner)
                    .name("Pet" + i)
                    .species(species.get(i - 1))
                    .breed(breeds.get(i - 1))
                    .dateOfBirth(LocalDate.of(2021, 9, 13))
                    .imageUrl(images.get(i - 1))
                    .build();
            petRepository.save(pet);
        }
    }
}
