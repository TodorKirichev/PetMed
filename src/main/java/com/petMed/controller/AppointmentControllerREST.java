package com.petMed.controller;

import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.User;
import com.petMed.service.AppointmentService;
import com.petMed.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointments")
public class AppointmentControllerREST {

    private final UserService userService;
    private final AppointmentService appointmentService;

    public AppointmentControllerREST(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/available-hours")
    public ResponseEntity<Map<String, List<String>>> getAvailableHours(@RequestParam String date, @RequestParam String vetUsername) {

        LocalDate selectedDate = LocalDate.parse(date);
        User vet = userService.findVetByUsername(vetUsername);

        List<Appointment> existingAppointments = appointmentService.findAvailableAppointmentsByDay(selectedDate, vet);
        List<String> availableTimes = existingAppointments.stream()
                .filter(appointment -> !appointment.isBooked())
                .map(appointment -> appointment.getStartTime().toString())
                .collect(Collectors.toList());

        Map<String, List<String>> response = new HashMap<>();
        response.put("times", availableTimes);

        return ResponseEntity.ok(response);
    }

}
