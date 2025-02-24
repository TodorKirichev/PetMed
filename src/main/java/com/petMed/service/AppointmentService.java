package com.petMed.service;

import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.User;
import com.petMed.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public String createDailySchedule(User vet, LocalDate date) {

        Optional<Appointment> byDate = appointmentRepository.findFirstByDate(date);

        if (byDate.isPresent()) {
            return "You already have daily schedule for this day";
        }

        LocalTime startTime = LocalTime.of(9,0);
        LocalTime endTime = LocalTime.of(17, 0);

        List<Appointment> appointments = new ArrayList<>();

        while (startTime.isBefore(endTime)) {
            Appointment appointment = Appointment.builder()
                    .vet(vet)
                    .date(date)
                    .startTime(startTime)
                    .endTime(startTime.plusHours(1))
                    .isBooked(false)
                    .build();

            appointments.add(appointment);
            startTime = startTime.plusHours(1);
        }

        appointmentRepository.saveAll(appointments);
        return "Successfully created daily schedule";
    }
}
