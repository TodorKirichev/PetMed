package com.petMed.IntegrationTests;

import com.petMed.clinic.model.CityName;
import com.petMed.clinic.service.ClinicService;
import com.petMed.cloudinary.CloudinaryService;
import com.petMed.scheduler.AppointmentScheduler;
import com.petMed.user.model.User;
import com.petMed.user.repository.UserRepository;
import com.petMed.user.service.UserService;
import com.petMed.web.dto.VetRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceITest {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClinicService clinicService;
    @Autowired
    private AppointmentScheduler appointmentScheduler;
    @MockitoBean
    private CloudinaryService cloudinaryService;

    @Test
    void registerVet_Success() {
        MockMultipartFile photo = new MockMultipartFile(
                "photo",
                "test-photo.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3, 4, 5}
        );

        VetRegisterRequest registerRequest = new VetRegisterRequest();
        registerRequest.setFirstName("Ivan");
        registerRequest.setLastName("Ivanov");
        registerRequest.setUsername("ivanov");
        registerRequest.setEmail("ivanov@gmail.com");
        registerRequest.setPhone("12345678");
        registerRequest.setPassword("12345");
        registerRequest.setClinicName("PetMed");
        registerRequest.setAddress("Ivan Vazov 55");
        registerRequest.setSite("www.petMed.com");
        registerRequest.setCity(CityName.PLOVDIV);
        registerRequest.setPhoto(photo);

        when(cloudinaryService.uploadFile(any())).thenReturn("http://image.com");

        userService.registerVet(registerRequest);

        Optional<User> user = userRepository.findByUsername(registerRequest.getUsername());

        assertTrue(user.isPresent());
        assertEquals(registerRequest.getUsername(), user.get().getUsername());
        assertNotNull(user.get().getClinic());
        assertEquals(registerRequest.getClinicName(), user.get().getClinic().getName());
    }
}
