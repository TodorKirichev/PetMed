package com.petMed.mapper;

import com.petMed.appointment.model.Appointment;
import com.petMed.web.dto.AppointmentInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppointmentMapper {

    public static AppointmentInfo mapAppointmentToAppointmentInfo(Appointment appointment) {
        return AppointmentInfo.builder()
                .appointmentId(appointment.getId())
                .startTime(appointment.getStartTime().toString())
                .petName(appointment.getPet().getName())
                .petSpecies(appointment.getPet().getSpecies().getSpeciesName())
                .petBreed(appointment.getPet().getBreed())
                .petOwnerFirstName(appointment.getPet().getOwner().getFirstName())
                .build();
    }
}
