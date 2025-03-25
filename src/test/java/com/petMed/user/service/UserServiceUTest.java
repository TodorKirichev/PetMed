package com.petMed.user.service;

import com.petMed.clinic.model.CityName;
import com.petMed.clinic.model.Clinic;
import com.petMed.clinic.service.ClinicService;
import com.petMed.cloudinary.CloudinaryService;
import com.petMed.exception.UserNotFoundException;
import com.petMed.scheduler.AppointmentScheduler;
import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import com.petMed.user.repository.UserRepository;
import com.petMed.web.dto.PetOwnerRegisterRequest;
import com.petMed.web.dto.VetData;
import com.petMed.web.dto.VetRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ClinicService clinicService;
    @Mock
    private AppointmentScheduler appointmentScheduler;
    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private UserService userService;

    private User vet1;
    private User vet2;
    private User vet3;
    private User vet4;
    private User vet5;

    @BeforeEach
    public void setUp() {
        Clinic clinic = new Clinic();
        clinic.setCity(CityName.PLOVDIV);

        vet1 = User.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .role(Role.VET)
                .clinic(clinic)
                .build();

        vet2 = User.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .role(Role.VET)
                .clinic(clinic)
                .build();

        vet3 = User.builder()
                .firstName("Asen")
                .lastName("Asenov")
                .role(Role.VET)
                .clinic(clinic)
                .build();

        vet4 = User.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .role(Role.VET)
                .build();

        vet5 = User.builder()
                .firstName("Asen")
                .lastName("Ivanov")
                .build();
    }

    @Test
    void searchVets_WithExistingNameAndCity() {
        String name = "Ivan";
        String city = "Plovdiv";
        when(userRepository.findAll()).thenReturn(List.of(vet1, vet2, vet3, vet4, vet5));

        List<User> vets = userService.searchVets(name, city);

        assertEquals(2, vets.size());
        assertEquals(name, vets.get(0).getFirstName());
        assertEquals(city, vets.get(0).getClinic().getCity().getCityName());
        assertEquals(name, vets.get(1).getFirstName());
        assertEquals(city, vets.get(1).getClinic().getCity().getCityName());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void searchVets_WithExistingNameOnly() {
        String name = "Asen";
        when(userRepository.findAll()).thenReturn(List.of(vet1, vet2, vet3, vet4, vet5));

        List<User> vets = userService.searchVets(name, null);

        assertEquals(1, vets.size());
        assertEquals(name, vets.get(0).getFirstName());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void searchVets_WithExistingCityOnly() {
        String city = "Plovdiv";
        when(userRepository.findAll()).thenReturn(List.of(vet1, vet2, vet3, vet4, vet5));

        List<User> vets = userService.searchVets(null, city);

        assertEquals(3, vets.size());
        assertEquals(city, vets.get(0).getClinic().getCity().getCityName());
        assertEquals(city, vets.get(1).getClinic().getCity().getCityName());
        assertEquals(city, vets.get(2).getClinic().getCity().getCityName());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void updateVetProfile_Success() {
        UUID vetId = UUID.randomUUID();
        vet1.setId(vetId);

        VetData vetData = VetData.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("123456789")
                .clinicName("PetMed")
                .city(CityName.PLOVDIV)
                .address("Ivan Vazov 55")
                .site("www.petMed.com")
                .build();

        when(userRepository.findById(vetId)).thenReturn(Optional.of(vet1));

        userService.updateVetProfile(vetId, vetData);

        verify(userRepository,times(1)).findById(vetId);
        verify(userRepository,times(1)).save(vet1);
    }

    @Test
    void changeRole_Success() {
        vet1.setRole(Role.PET_OWNER);
        String username = "ivanov";
        String newRole = Role.VET.toString();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(vet1));

        userService.changeRole(username, newRole);
        Optional<User> byUsername = userRepository.findByUsername(username);

        assertNotNull(byUsername);
        assertEquals(newRole, byUsername.get().getRole().name());
        verify(userRepository,times(1)).save(vet1);
    }

    @Test
    void findAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(List.of(vet1, vet2, vet3, vet4));

        List<User> result = userService.findAllUsers();

        assertEquals(4, result.size());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void findByUsername_Success() {
        String username = "ivanov";
        vet1.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(vet1));

        User result = userService.findByUsername(username);

        assertEquals(username, result.getUsername());
        verify(userRepository,times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_ShouldThrow_WhenNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername(any()));
    }

    @Test
    void findAllVets_Success() {
        when(userRepository.findAllByRole(Role.VET)).thenReturn(List.of(vet1, vet2, vet3, vet4));

        List<User> result = userService.findAllVets();

        assertEquals(4, result.size());
        verify(userRepository,times(1)).findAllByRole(Role.VET);
    }

    @Test
    void loadUserByUsername_Success() {
        UUID userId = UUID.randomUUID();
        String username = "ivanov";
        vet2.setId(userId);
        vet1.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(vet1));

        UserDetails result = userService.loadUserByUsername(username);

        assertEquals(vet1.getUsername(), result.getUsername());
        verify(userRepository,times(1)).findByUsername(username);
    }

    @Test
    void findById_Success() {
        UUID userId = UUID.randomUUID();
        vet1.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(vet1));

        User result = userService.findById(userId);

        assertEquals(vet1.getId(), result.getId());
        verify(userRepository,times(1)).findById(userId);
    }

    @Test
    void findById_ShouldThrow_WhenNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(any()));
    }

    @Test
    void registerOwner_Success() {
        PetOwnerRegisterRequest registerRequest = new PetOwnerRegisterRequest();
        registerRequest.setFirstName("Ivan");
        registerRequest.setLastName("Ivanov");
        registerRequest.setUsername("ivanov");
        registerRequest.setEmail("ivanov@gmail.com");
        registerRequest.setPhone("123456789");
        registerRequest.setPassword("12345");

        userService.registerOwner(registerRequest);

        verify(userRepository,times(1)).save(any());
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    void registerVet_Success() {
        String imageUrl = "www.image.com";
        when(cloudinaryService.uploadFile(any())).thenReturn(imageUrl);

        VetRegisterRequest registerRequest = new VetRegisterRequest();
        registerRequest.setFirstName("Ivan");
        registerRequest.setLastName("Ivanov");
        registerRequest.setUsername("ivanov");
        registerRequest.setEmail("ivanov@gmail.com");
        registerRequest.setPhone("123456789");
        registerRequest.setPassword("12345");
        registerRequest.setClinicName("PetMed");
        registerRequest.setAddress("Ivan Vazov 55");
        registerRequest.setSite("www.petMed.com");
        registerRequest.setCity(CityName.PLOVDIV);

        userService.registerVet(registerRequest);

        verify(userRepository,times(1)).save(any());
        verify(appointmentScheduler, times(1)).generateAppointmentsForVetOnRegistration(any());
        verify(eventPublisher,times(1)).publishEvent(any());
    }
}
