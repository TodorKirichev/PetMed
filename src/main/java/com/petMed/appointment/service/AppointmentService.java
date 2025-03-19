package com.petMed.appointment.service;

import com.petMed.exception.AppointmentNotFoundException;
import com.petMed.mapper.AppointmentMapper;
import com.petMed.web.dto.AppointmentInfo;
import com.petMed.web.dto.AppointmentData;
import com.petMed.appointment.model.Appointment;
import com.petMed.pet.model.Pet;
import com.petMed.user.model.User;
import com.petMed.appointment.model.AppointmentStatus;
import com.petMed.appointment.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public void book(User vet, Pet pet, AppointmentData appointmentData) {
        Appointment appointment = findByVetAndDateAndTime(vet, appointmentData.getDate(), appointmentData.getTime());
        appointment.setPet(pet);
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointmentRepository.save(appointment);
    }

    public Appointment findByVetAndDateAndTime(User vet, LocalDate date, LocalTime time) {
        return appointmentRepository.findByVetAndDateAndStartTime(vet, date, time);
    }

    @Transactional
    public void freeUp(List<Appointment> appointments) {
        appointments.forEach(appointment -> {
            appointment.setPet(null);
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            appointmentRepository.save(appointment);
        });
    }

    public List<AppointmentInfo> findAllBookedAppointmentsByDayAndVet(LocalDate currentDate, User vet) {
        return findAllAppointmentsByDayAndVet(currentDate, vet)
                .stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.BOOKED)
                .map(AppointmentMapper::mapAppointmentToAppointmentInfo)
                .collect(Collectors.toList());
    }

    public List<String> findAllAvailableTimesForAppointmentByDayAndVet(LocalDate selectedDate, User vet) {
        return findAllAppointmentsByDayAndVet(selectedDate, vet)
                .stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.SCHEDULED)
                .map(appointment -> appointment.getStartTime().toString())
                .collect(Collectors.toList());
    }

    public List<Appointment> findAllAppointmentsByDayAndVet(LocalDate currentDay, User user) {
        return appointmentRepository.findByDateAndVetOrderByStartTimeAsc(currentDay, user);
    }

    @Transactional
    public void changeStatusToCompleted(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    public Appointment findById(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new AppointmentNotFoundException("Appointment not found");
        }
        return appointment.get();
    }
}
