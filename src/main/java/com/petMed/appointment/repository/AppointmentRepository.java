package com.petMed.appointment.repository;

import com.petMed.appointment.model.Appointment;
import com.petMed.user.model.User;
import com.petMed.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByDateAndVetOrderByStartTimeAsc(LocalDate now, User user);

    List<Appointment> findAllByDateAndVetOrderByStartTime(LocalDate day, User vet);

    Appointment findByVetAndDateAndStartTime(User vet, LocalDate date, LocalTime time);

    boolean existsByVetAndDate(User vet, LocalDate date);

    List<Appointment> findByDateBeforeAndStatus(LocalDate yesterday, AppointmentStatus appointmentStatus);
}
