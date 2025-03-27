package com.petMed.mapper;

import com.petMed.appointment.model.Appointment;
import com.petMed.web.dto.AppointmentInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppointmentMapper {

    public static AppointmentInfo mapAppointmentToAppointmentInfo(Appointment appointment) {
        boolean hasPet = appointment.getPet() != null;

        return AppointmentInfo.builder()
                .appointmentId(appointment.getId())
                .date(appointment.getDate().toString())
                .startTime(appointment.getStartTime().toString())
                .petName(hasPet ? appointment.getPet().getName() : null)
                .petSpecies(hasPet ? appointment.getPet().getSpecies().getSpeciesName() : null)
                .petBreed(hasPet ? appointment.getPet().getBreed() : null)
                .petOwnerName(hasPet ? appointment.getPet().getOwner().getFirstName() + " " + appointment.getPet().getOwner().getLastName() : null)
                .hasMedicalRecord(hasPet && appointment.getMedicalRecord() != null)
                .build();
    }
}
