package com.petMed.web;

import com.petMed.web.dto.AppointmentInfo;
import com.petMed.user.model.User;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    public ResponseEntity<List<AppointmentInfo>> getAllByDate(@RequestParam String date, @RequestParam String vetUsername) {
        LocalDate currentDate = LocalDate.parse(date);
        User vet = userService.findVetByUsername(vetUsername);
        List<AppointmentInfo> allAppointmentsByDay = appointmentService.findAllBookedAppointmentsByDayAndVet(currentDate, vet);

        return ResponseEntity.ok(allAppointmentsByDay);
    }

}
