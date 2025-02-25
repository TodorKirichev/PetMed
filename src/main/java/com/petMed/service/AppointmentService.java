package com.petMed.service;

import com.petMed.model.dto.AppointmentData;
import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.Pet;
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


    public List<Appointment> findAllAppointmentsForToday(User user) {
        return appointmentRepository.findByDateAndVetOrderByDateAscStartTimeAsc(LocalDate.now(), user);
    }

    public List<Appointment> findAvailableAppointmentsByDay(LocalDate day, User vet) {
        return appointmentRepository.findAllByDateAndVetOrderByStartTime(day, vet);
    }

    public Appointment findByVetAndDateAndTime(User vet, LocalDate date, LocalTime time) {
        return appointmentRepository.findByVetAndDateAndStartTime(vet, date, time);
    }

    public void book(User vet, Pet pet, AppointmentData appointmentData) {
        Appointment appointment = findByVetAndDateAndTime(vet, appointmentData.getDate(), appointmentData.getTime());
        appointment.setPet(pet);
        appointment.setBooked(true);
        appointmentRepository.save(appointment);
    }
}
