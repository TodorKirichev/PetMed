package com.petMed.mapper;

import com.petMed.appointment.model.Appointment;
import com.petMed.web.dto.AppointmentInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppointmentMapper {

    public static AppointmentInfo mapAppointmentToAppointmentInfo(Appointment appointment) {
        return AppointmentInfo.builder()
                .appointmentId(appointment.getId())
                .date(appointment.getDate().toString())
                .startTime(appointment.getStartTime().toString())
                .petName(appointment.getPet() != null ? appointment.getPet().getName() : null)
                .petSpecies(appointment.getPet() != null ? appointment.getPet().getSpecies().getSpeciesName() : null)
                .petBreed(appointment.getPet() != null ? appointment.getPet().getBreed() : null)
                .petOwnerFirstName(appointment.getPet() != null ? appointment.getPet().getOwner().getFirstName() : null)
                .build();
    }
}
