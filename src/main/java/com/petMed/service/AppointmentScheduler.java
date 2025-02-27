package com.petMed.service;

import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.User;
import com.petMed.model.enums.AppointmentStatus;
import com.petMed.model.enums.Role;
import com.petMed.repository.AppointmentRepository;
import com.petMed.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentScheduler {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentScheduler(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void generateAppointments() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(2);

        LocalDate date = startDate;
        while (date.isBefore(endDate)) {
            if (!isWeekend(date)) {
                generateAppointmentsForDay(date);
            }
            date = date.plusDays(1);
        }
    }

    private void generateAppointmentsForDay(LocalDate date) {
        List<User> vets = userRepository.findAllByRole(Role.VET);

        for (User vet : vets) {
            boolean hasAppointments = appointmentRepository.existsByVetAndDate(vet, date);

            if (!hasAppointments) {
                generateAppointmentsForVet(vet, date);
            }
        }
    }

    private void generateAppointmentsForVet(User vet, LocalDate date) {
        for (int hour = 9; hour <= 16; hour++) {
            LocalTime startTime = LocalTime.of(hour, 0);
            createAppointment(vet, date, startTime);
        }
    }

    private void createAppointment(User vet, LocalDate date, LocalTime startTime) {
        Appointment appointment = Appointment.builder()
                .date(date)
                .startTime(startTime)
                .status(AppointmentStatus.SCHEDULED)
                .vet(vet)
                .build();

        appointmentRepository.save(appointment);
    }


    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
