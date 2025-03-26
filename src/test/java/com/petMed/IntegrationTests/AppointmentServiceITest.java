package com.petMed.IntegrationTests;

import com.petMed.appointment.model.Appointment;
import com.petMed.appointment.model.AppointmentStatus;
import com.petMed.appointment.repository.AppointmentRepository;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.clinic.model.CityName;
import com.petMed.clinic.model.Clinic;
import com.petMed.clinic.repository.ClinicRepository;
import com.petMed.mapper.AppointmentMapper;
import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import com.petMed.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AppointmentServiceITest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private ClinicRepository clinicRepository;

    @Test
    void findAllAppointmentsInfoByDayAndVet_Success() {
        Clinic clinic = Clinic.builder()
                .name("Clinic")
                .city(CityName.PLOVDIV)
                .address("Ivan Vazov")
                .site("http://site.com")
                .build();
        clinicRepository.save(clinic);

        User vet = User.builder()
                .firstName("Vet")
                .lastName("Schneider")
                .username("Vet")
                .password("password")
                .email("Vet@Schneider")
                .phone("1236789")
                .role(Role.VET)
                .imageUrl("http://image.com")
                .clinic(clinic)
                .build();
        Appointment appointment1 = Appointment.builder()
                .date(LocalDate.now())
                .startTime(LocalTime.of(9, 0))
                .status(AppointmentStatus.SCHEDULED)
                .build();
        Appointment appointment2 = Appointment.builder()
                .date(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        appointment1.setVet(vet);
        appointment1.setDate(LocalDate.now());
        appointment2.setVet(vet);
        appointment2.setDate(LocalDate.now());

        userRepository.save(vet);
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);

        List<Appointment> allAppointmentsByDayAndVet = appointmentService.findAllAppointmentsByDayAndVet(LocalDate.now(), vet);

        assertEquals(2, allAppointmentsByDayAndVet.size());
        assertEquals(appointment1.getDate(), allAppointmentsByDayAndVet.get(0).getDate());
        assertEquals(appointment2.getDate(), allAppointmentsByDayAndVet.get(1).getDate());
    }
}
