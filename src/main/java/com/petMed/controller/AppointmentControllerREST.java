package com.petMed.controller;

import com.petMed.model.dto.AppointmentDTO;
import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.User;
import com.petMed.model.enums.AppointmentStatus;
import com.petMed.service.AppointmentService;
import com.petMed.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentControllerREST {

    private final UserService userService;
    private final AppointmentService appointmentService;

    public AppointmentControllerREST(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/available-hours")
    public ResponseEntity<List<String>> getAvailableHours(@RequestParam String date, @RequestParam String vetUsername) {
        LocalDate selectedDate = LocalDate.parse(date);
        User vet = userService.findVetByUsername(vetUsername);
        List<String> availableTimes = appointmentService.findAllAvailableTimesForAppointmentByDayAndVet(selectedDate, vet);

        return ResponseEntity.ok(availableTimes);
    }

    @GetMapping("/date")
    public ResponseEntity<List<AppointmentDTO>> getAllByDate(@RequestParam String date, @RequestParam String vetUsername) {
        LocalDate currentDate = LocalDate.parse(date);
        User vet = userService.findVetByUsername(vetUsername);
        List<AppointmentDTO> allAppointmentsByDay = appointmentService.findAllBookedAppointmentsByDayAndVet(currentDate, vet);

        return ResponseEntity.ok(allAppointmentsByDay);
    }

}
