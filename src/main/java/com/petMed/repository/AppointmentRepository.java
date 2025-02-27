package com.petMed.repository;

import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<Appointment> findFirstByDate(LocalDate date);

    List<Appointment> findByDateAndVetOrderByDateAscStartTimeAsc(LocalDate now, User user);

    List<Appointment> findAllByDateAndVetOrderByStartTime(LocalDate day, User vet);

    Appointment findByVetAndDateAndStartTime(User vet, LocalDate date, LocalTime time);

    boolean existsByVetAndDate(User vet, LocalDate date);
}
