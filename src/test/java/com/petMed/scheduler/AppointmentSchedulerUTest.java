package com.petMed.scheduler;

import com.petMed.appointment.model.Appointment;
import com.petMed.appointment.model.AppointmentStatus;
import com.petMed.appointment.repository.AppointmentRepository;
import com.petMed.user.model.User;
import com.petMed.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentSchedulerUTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentScheduler appointmentScheduler;

    @Test
    void testGenerateAppointmentsForVetOnRegistration() {
        User vet = new User();

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(2);

        appointmentScheduler.generateAppointmentsForVetOnRegistration(vet);

        ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository, atLeastOnce()).save(captor.capture());

        List<Appointment> savedAppointments = captor.getAllValues();
        assertFalse(savedAppointments.isEmpty());

        for (Appointment appointment : savedAppointments) {
            assertEquals(vet, appointment.getVet());
            assertTrue(appointment.getDate().isAfter(startDate.minusDays(1)));
            assertTrue(appointment.getDate().isBefore(endDate));
            assertNotNull(appointment.getStartTime());
            assertEquals(AppointmentStatus.SCHEDULED, appointment.getStatus());
        }
    }

    @Test
    void cleanUpOldAppointments_Success() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        Appointment appointment1 = new Appointment();
        appointment1.setStatus(AppointmentStatus.SCHEDULED);
        appointment1.setDate(yesterday);

        Appointment appointment2 = new Appointment();
        appointment2.setStatus(AppointmentStatus.SCHEDULED);
        appointment2.setDate(yesterday);

        when(appointmentRepository.findByDateBeforeAndStatus(yesterday, AppointmentStatus.SCHEDULED))
                .thenReturn(List.of(appointment1, appointment2));

        appointmentScheduler.cleanUpOldAppointments();

        verify(appointmentRepository, times(1)).deleteAll(any());
    }

    @Test
    void cleanUpOldAppointments_WhenNoOldAppointments() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        when(appointmentRepository.findByDateBeforeAndStatus(yesterday, AppointmentStatus.SCHEDULED))
                .thenReturn(new ArrayList<>());

        appointmentScheduler.cleanUpOldAppointments();

        verify(appointmentRepository, times(0)).deleteAll(any());
    }
}
