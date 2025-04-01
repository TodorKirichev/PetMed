package com.petMed.mapper;

import com.petMed.appointment.model.Appointment;
import com.petMed.web.dto.AppointmentInfo;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.Period;

@UtilityClass
public class AppointmentMapper {

    public static AppointmentInfo mapAppointmentToAppointmentInfo(Appointment appointment) {
        boolean hasPet = appointment.getPet() != null;

        return AppointmentInfo.builder()
                .appointmentId(appointment.getId())
                .date(appointment.getDate().toString())
                .startTime(appointment.getStartTime().toString())
                .reason(hasPet ? appointment.getReason() : null)
                .petName(hasPet ? appointment.getPet().getName() : null)
                .petSpecies(hasPet ? appointment.getPet().getSpecies().getSpeciesName() : null)
                .petBreed(hasPet ? appointment.getPet().getBreed() : null)
                .petAge(hasPet ? getAge(appointment) : null)
                .petOwnerName(hasPet ? appointment.getPet().getOwner().getFirstName() + " " + appointment.getPet().getOwner().getLastName() : null)
                .petOwnerEmail(hasPet ? appointment.getPet().getOwner().getEmail() : null)
                .petOwnerPhone(hasPet ? appointment.getPet().getOwner().getPhone() : null)
                .petImageUrl(hasPet ? appointment.getPet().getImageUrl() : null)
                .hasMedicalRecord(hasPet && appointment.getMedicalRecord() != null)
                .build();
    }

    private String getAge(Appointment appointment) {
        String age;

        Period period = Period.between(appointment.getPet().getDateOfBirth(), LocalDate.now());
        if (period.getYears() < 1) {
            if (period.getMonths() < 1) {
                age = period.getDays() + " days";
            } else {
                age = period.getMonths() + " months";
            }
        } else {
            age = period.getYears() + " years";
        }
        return age;
    }
}
