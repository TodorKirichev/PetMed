package com.petMed.user.service;

import com.petMed.clinic.service.ClinicService;
import com.petMed.cloudinary.CloudinaryService;
import com.petMed.event.UserRegisterEvent;
import com.petMed.exception.UserNotFoundException;
import com.petMed.scheduler.AppointmentScheduler;
import com.petMed.web.dto.*;
import com.petMed.clinic.model.Clinic;
import com.petMed.user.model.User;
import com.petMed.user.model.Role;
import com.petMed.user.repository.UserRepository;
import com.petMed.security.CurrentUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClinicService clinicService;
    private final AppointmentScheduler appointmentScheduler;
    private final CloudinaryService cloudinaryService;

    public UserService(ApplicationEventPublisher eventPublisher,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ClinicService clinicService,
                       AppointmentScheduler appointmentScheduler, CloudinaryService cloudinaryService) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clinicService = clinicService;
        this.appointmentScheduler = appointmentScheduler;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public void registerOwner(PetOwnerRegisterRequest petOwnerRegisterRequest) {
        User owner = createUser(petOwnerRegisterRequest);
        owner.setRole(Role.PET_OWNER);
        userRepository.save(owner);
        eventPublisher.publishEvent(new UserRegisterEvent(owner));
    }

    @Transactional
    public void registerVet(VetRegisterRequest vetRegisterRequest) {
        String imageUrl = cloudinaryService.uploadFile(vetRegisterRequest.getPhoto());

        User vet = createUser(vetRegisterRequest);
        vet.setImageUrl(imageUrl);
        vet.setRole(Role.VET);

        Clinic clinic = createClinic(vetRegisterRequest);
        vet.setClinic(clinic);

        userRepository.save(vet);
        appointmentScheduler.generateAppointmentsForVetOnRegistration(vet);
        eventPublisher.publishEvent(new UserRegisterEvent(vet));
    }

    private Clinic createClinic(VetRegisterRequest vetRegisterRequest) {
        return clinicService.createClinic(
                vetRegisterRequest.getClinicName(),
                vetRegisterRequest.getCity(),
                vetRegisterRequest.getAddress(),
                vetRegisterRequest.getSite()
        );
    }

    private User createUser(RegisterRequest registerRequest) {
        return User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .isActive(true)
                .build();
    }

    public User findById(UUID userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return byId.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return new CurrentUser(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
    }

    public List<User> findAllVets() {
        return userRepository.findAllByRole(Role.VET);
    }

    public User findByUsername(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return byUsername.get();
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void changeRole(String username, String newRole) {
        User byUsername = findByUsername(username);
        byUsername.setRole(Role.valueOf(newRole));
        userRepository.save(byUsername);
    }

    @Transactional
    public void updateVetProfile(UUID userId, VetData vetData) {
        User vet = findById(userId);
        Clinic clinic = clinicService.createClinic(
                vetData.getClinicName(),
                vetData.getCity(),
                vetData.getAddress(),
                vetData.getSite()
        );

        vet.setId(vet.getId());
        vet.setFirstName(vetData.getFirstName());
        vet.setLastName(vetData.getLastName());
        vet.setPhone(vetData.getPhone());
        vet.setClinic(clinic);

        userRepository.save(vet);
        if (vet.getClinic() != null) {
            appointmentScheduler.generateAppointmentsForVetOnRegistration(vet);
        }
    }

    public List<User> searchVets(String name, String city) {
        List<User> allVets = userRepository.findAll();

        return allVets.stream()
                .filter(user -> user.getRole() == Role.VET)
                .filter(user -> user.getClinic() != null)
                .filter(user -> (name == null || user.getFirstName().toLowerCase().contains(name.toLowerCase().trim()) || user.getLastName().toLowerCase().contains(name.toLowerCase())))
                .filter(user -> (city == null || user.getClinic().getCity().getCityName().toLowerCase().contains(city.toLowerCase().trim())))
                .collect(Collectors.toList());
    }
}