package com.petMed.appointment.service;

import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.web.dto.AppointmentInfo;
import com.petMed.web.dto.AppointmentData;
import com.petMed.appointment.model.Appointment;
import com.petMed.pet.model.Pet;
import com.petMed.user.model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    void book(User vet, Pet pet, AppointmentData appointmentData);

    Appointment findByVetAndDateAndTime(User vet, LocalDate date, LocalTime time);

    void freeUp(List<Appointment> appointments);

    List<AppointmentInfo> findAllAppointmentsInfoByDayAndVet(LocalDate currentDate, User vet);

    List<String> findAllScheduledTimesForAppointmentByDayAndVet(LocalDate selectedDate, User vet);

    List<Appointment> findAllAppointmentsByDayAndVet(LocalDate currentDay, User user);

    void changeStatusToCompleted(Appointment appointment, MedicalRecord medicalRecord);

    Appointment findById(UUID appointmentId);
}
