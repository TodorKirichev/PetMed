package com.petMed.appointment.service.impl;

import com.petMed.appointment.service.AppointmentService;
import com.petMed.exception.AppointmentNotFoundException;
import com.petMed.mapper.AppointmentMapper;
import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.notification.service.NotificationService;
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
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void book(User vet, Pet pet, AppointmentData appointmentData) {
        Appointment appointment = findByVetAndDateAndTime(vet, appointmentData.getDate(), appointmentData.getTime());
        appointment.setPet(pet);
        appointment.setReason(appointmentData.getReason());
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointmentRepository.save(appointment);
        notificationService.sendNotification(appointment);
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

    public List<AppointmentInfo> findAllAppointmentsInfoByDayAndVet(LocalDate currentDate, User vet) {
        return findAllAppointmentsByDayAndVet(currentDate, vet)
                .stream()
                .map(AppointmentMapper::mapAppointmentToAppointmentInfo)
                .collect(Collectors.toList());
    }

    public List<String> findAllScheduledTimesForAppointmentByDayAndVet(LocalDate selectedDate, User vet) {
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
    public void changeStatusToCompleted(Appointment appointment, MedicalRecord medicalRecord) {
        appointment.setMedicalRecord(medicalRecord);
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
