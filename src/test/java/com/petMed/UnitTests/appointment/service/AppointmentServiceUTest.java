package com.petMed.UnitTests.appointment.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.appointment.model.AppointmentStatus;
import com.petMed.appointment.repository.AppointmentRepository;
import com.petMed.appointment.service.impl.AppointmentServiceImpl;
import com.petMed.exception.AppointmentNotFoundException;
import com.petMed.mapper.AppointmentMapper;
import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.notification.service.NotificationService;
import com.petMed.pet.model.Pet;
import com.petMed.pet.model.PetSpecies;
import com.petMed.user.model.User;
import com.petMed.web.dto.AppointmentData;
import com.petMed.web.dto.AppointmentInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceUTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private UUID appointmentId1;
    private UUID appointmentId2;
    private UUID appointmentId3;
    private Appointment appointment1;
    private Appointment appointment2;
    private Appointment appointment3;
    private LocalDate date;
    private User vet;
    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        appointmentId1 = UUID.randomUUID();
        appointmentId2 = UUID.randomUUID();
        appointmentId3 = UUID.randomUUID();
        appointment1 = new Appointment();
        appointment2 = new Appointment();
        appointment3 = new Appointment();
        date = LocalDate.now();
        vet = new User();
        pet1 = new Pet();
        pet2 = new Pet();
    }

    @Test
    void shouldReturnAppointment_WhenExists() {
        appointment1.setId(appointmentId1);

        when(appointmentRepository.findById(appointmentId1)).thenReturn(Optional.of(appointment1));

        Appointment result = appointmentService.findById(appointmentId1);
        assertEquals(result.getId(), appointment1.getId());
        verify(appointmentRepository, times(1)).findById(appointmentId1);
    }

    @Test
    void shouldThrow_WhenAppointmentDoesNotExists() {
        when(appointmentRepository.findById(appointmentId1)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.findById(appointmentId1));
        verify(appointmentRepository, times(1)).findById(appointmentId1);
    }

    @Test
    void changeStatusToCompleted_Success() {
        Appointment appointment = new Appointment();
        MedicalRecord medicalRecord = new MedicalRecord();
        appointmentService.changeStatusToCompleted(appointment, medicalRecord);

        assertEquals(appointment.getStatus(), AppointmentStatus.COMPLETED);
        verify(appointmentRepository,times(1)).save(appointment);
    }

    @Test
    void findAllAppointmentsByDayAndVet_Success() {
        appointment1.setId(appointmentId1);
        appointment1.setDate(date);
        appointment1.setVet(vet);

        appointment2.setId(appointmentId2);
        appointment2.setDate(date);
        appointment2.setVet(vet);

        when(appointmentRepository.findByDateAndVetOrderByStartTimeAsc(date, vet))
                .thenReturn(List.of(appointment1, appointment2));

        List<Appointment> appointments = appointmentService
                .findAllAppointmentsByDayAndVet(date, vet);

        assertEquals(appointments.size(), 2);
        assertEquals(appointments.get(0).getId(), appointment1.getId());
        assertEquals(appointments.get(1).getId(), appointment2.getId());
        verify(appointmentRepository, times(1)).findByDateAndVetOrderByStartTimeAsc(date, vet);
    }

    @Test
    void findAllAppointmentsByDayAndVet_ShouldReturnEmptyList_WhenNoAppointments() {
        when(appointmentRepository.findByDateAndVetOrderByStartTimeAsc(date, vet)).thenReturn(new ArrayList<>());

        List<Appointment> appointments = appointmentService.findAllAppointmentsByDayAndVet(date, vet);

        assertTrue(appointments.isEmpty());
        verify(appointmentRepository, times(1)).findByDateAndVetOrderByStartTimeAsc(date, vet);
    }

    @Test
    void findAllScheduledTimesForAppointmentByDayAndVet_Success() {
        appointment1.setId(appointmentId1);
        appointment1.setDate(date);
        appointment1.setVet(vet);
        appointment1.setStatus(AppointmentStatus.SCHEDULED);
        appointment1.setStartTime(LocalTime.of(9, 0));

        appointment2.setId(appointmentId2);
        appointment2.setDate(date);
        appointment2.setVet(vet);
        appointment2.setStatus(AppointmentStatus.SCHEDULED);
        appointment2.setStartTime(LocalTime.of(10, 0));

        appointment3.setId(appointmentId3);
        appointment3.setDate(date);
        appointment3.setVet(vet);
        appointment3.setStatus(AppointmentStatus.COMPLETED);
        appointment3.setStartTime(LocalTime.of(11, 0));

        when(appointmentService.findAllAppointmentsByDayAndVet(date, vet))
                .thenReturn(List.of(appointment1, appointment2, appointment3));

        List<String> availableTimes = appointmentService.findAllScheduledTimesForAppointmentByDayAndVet(date, vet);

        assertEquals(availableTimes.size(), 2);
        assertEquals(availableTimes.get(0), "09:00");
        assertEquals(availableTimes.get(1), "10:00");
    }

    @Test
    void findAllScheduledTimesForAppointmentByDayAndVet_ShouldReturnEmptyList_WhenNoAppointments() {
        when(appointmentService.findAllAppointmentsByDayAndVet(date, vet))
                .thenReturn(new ArrayList<>());

        List<String> availableTimes = appointmentService.findAllScheduledTimesForAppointmentByDayAndVet(date, vet);

        assertTrue(availableTimes.isEmpty());
    }

    @Test
    void findAllAppointmentsInfoByDayAndVet_Success() {
        appointment1.setId(appointmentId1);
        appointment1.setDate(date);
        appointment1.setStartTime(LocalTime.of(9, 0));
        appointment1.setDate(date);
        appointment1.setVet(vet);

        appointment2.setId(appointmentId2);
        appointment2.setDate(date);
        appointment2.setStartTime(LocalTime.of(10, 0));
        appointment2.setDate(date);
        appointment2.setVet(vet);

        when(appointmentService.findAllAppointmentsByDayAndVet(date, vet))
                .thenReturn(List.of(appointment1, appointment2));

        AppointmentInfo appointmentInfo1 = AppointmentInfo.builder()
                .appointmentId(appointmentId1)
                .build();
        AppointmentInfo appointmentInfo2 = AppointmentInfo.builder()
                .appointmentId(appointmentId2)
                .build();

        try (MockedStatic<AppointmentMapper> mockedMapper = mockStatic(AppointmentMapper.class)) {
            mockedMapper.when(() -> AppointmentMapper.mapAppointmentToAppointmentInfo(appointment1))
                    .thenReturn(appointmentInfo1);
            mockedMapper.when(() -> AppointmentMapper.mapAppointmentToAppointmentInfo(appointment2))
                    .thenReturn(appointmentInfo2);

            List<AppointmentInfo> allBookedAppointments = appointmentService.findAllAppointmentsInfoByDayAndVet(date, vet);

            assertEquals(2, allBookedAppointments.size());
            assertEquals(appointment1.getId(), allBookedAppointments.get(0).getAppointmentId());
            assertEquals(appointment2.getId(), allBookedAppointments.get(1).getAppointmentId());
        }
    }

    @Test
    void freeUpAppointments_Success() {
        appointment1.setPet(pet1);
        appointment1.setStatus(AppointmentStatus.BOOKED);
        appointment2.setPet(pet2);
        appointment2.setStatus(AppointmentStatus.BOOKED);

        appointmentService.freeUp(List.of(appointment1, appointment2));

        assertEquals(AppointmentStatus.SCHEDULED, appointment1.getStatus());
        assertNull(appointment1.getPet());
        assertEquals(AppointmentStatus.SCHEDULED, appointment2.getStatus());
        assertNull(appointment2.getPet());

        verify(appointmentRepository, times(2)).save(any(Appointment.class));
    }

    @Test
    void findByVetAndDateAndTime_Success() {
        appointment1.setId(appointmentId1);
        appointment1.setDate(date);
        appointment1.setVet(vet);
        appointment1.setStartTime(LocalTime.of(9, 0));

        when(appointmentRepository.findByVetAndDateAndStartTime(vet, date, LocalTime.of(9, 0)))
                .thenReturn(appointment1);

        Appointment result = appointmentService.findByVetAndDateAndTime(vet, date, LocalTime.of(9, 0));

        assertEquals(appointment1.getId(), result.getId());
        assertEquals(appointment1.getDate(), result.getDate());
        assertEquals(appointment1.getStartTime(), result.getStartTime());

        verify(appointmentRepository,times(1)).findByVetAndDateAndStartTime(vet, date, LocalTime.of(9, 0));
    }

    @Test
    void findByVetAndDateAndTime_ShouldReturnNull_WhenNoAppointments() {
        when(appointmentRepository.findByVetAndDateAndStartTime(vet, date, LocalTime.of(9, 0)))
                .thenReturn(null);

        Appointment result = appointmentService.findByVetAndDateAndTime(vet, date, LocalTime.of(9, 0));

        assertNull(result);
        verify(appointmentRepository, times(1)).findByVetAndDateAndStartTime(vet, date, LocalTime.of(9, 0));
    }

    @Test
    void book_Success() {
        vet.setFirstName("Ivan");
        vet.setLastName("Ivanov");

        User owner = User.builder()
                .email("owner@gmail.com")
                .build();
        pet1.setId(UUID.randomUUID());
        pet1.setSpecies(PetSpecies.CAT);
        pet1.setOwner(owner);

        appointment1.setId(appointmentId1);
        appointment1.setPet(null);
        appointment1.setStatus(AppointmentStatus.SCHEDULED);
        appointment1.setVet(vet);

        AppointmentData appointmentData = new AppointmentData();
        appointmentData.setDate(date);
        appointmentData.setTime(LocalTime.of(9, 0));

        when(appointmentService.findByVetAndDateAndTime(vet, date, LocalTime.of(9, 0))).thenReturn(appointment1);
        doNothing().when(notificationService).sendNotification(any());

        appointmentService.book(vet, pet1, appointmentData);

        assertEquals(AppointmentStatus.BOOKED, appointment1.getStatus());
        assertEquals(pet1.getId(), appointment1.getPet().getId());
        verify(appointmentRepository,times(1)).save(appointment1);
    }
}
