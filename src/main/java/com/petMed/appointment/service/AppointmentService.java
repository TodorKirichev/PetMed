package com.petMed.appointment.service;

import com.petMed.mapper.AppointmentMapper;
import com.petMed.web.dto.AppointmentInfo;
import com.petMed.web.dto.AppointmentData;
import com.petMed.appointment.model.Appointment;
import com.petMed.pet.model.Pet;
import com.petMed.user.model.User;
import com.petMed.appointment.model.AppointmentStatus;
import com.petMed.appointment.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

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

//    public String createDailySchedule(User vet, LocalDate date) {
//
//        Optional<Appointment> byDate = appointmentRepository.findFirstByDate(date);
//
//        if (byDate.isPresent()) {
//            return "You already have daily schedule for this day";
//        }
//
//        LocalTime startTime = LocalTime.of(9,0);
//        LocalTime endTime = LocalTime.of(17, 0);
//
//        List<Appointment> appointments = new ArrayList<>();
//
//        while (startTime.isBefore(endTime)) {
//            Appointment appointment = Appointment.builder()
//                    .vet(vet)
//                    .date(date)
//                    .startTime(startTime)
//                    .status(AppointmentStatus.SCHEDULED)
//                    .build();
//
//            appointments.add(appointment);
//            startTime = startTime.plusHours(1);
//        }
//
//        appointmentRepository.saveAll(appointments);
//        return "Successfully created daily schedule";
//    }


    public List<Appointment> findAllAppointmentsByDayAndVet(LocalDate currentDay, User user) {
        return appointmentRepository.findByDateAndVetOrderByStartTimeAsc(currentDay, user);
    }

    public Appointment findByVetAndDateAndTime(User vet, LocalDate date, LocalTime time) {
        return appointmentRepository.findByVetAndDateAndStartTime(vet, date, time);
    }

    public void book(User vet, Pet pet, AppointmentData appointmentData) {
        Appointment appointment = findByVetAndDateAndTime(vet, appointmentData.getDate(), appointmentData.getTime());
        appointment.setPet(pet);
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointmentRepository.save(appointment);
    }

    public void freeUp(List<Appointment> appointments) {
        appointments.forEach(appointment -> {
            appointment.setPet(null);
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            appointmentRepository.save(appointment);
        });
    }

    public Appointment findById(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }
        return appointment.get();
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

    public void changeStatusToCompleted(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }
}
