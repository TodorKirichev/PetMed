package com.petMed.email;

public class EmailTemplates {

    public static final String CONFIRM_REGISTRATION_SUBJECT = "Welcome to Pet Med!";

    public static final String CONFIRM_REGISTRATION_BODY = "Hi %s %s,\n" +
            "\n" +
            "Thank you for signing up for Pet Med!\n" +
            "\n" +
            "If you did not create this account, please ignore this email.\n" +
            "\n" +
            "Best regards,  \n" +
            "The Pet Med Team  \n" +
            "http://localhost:8080/  \n";

    public static final String APPOINTMENT_BOOKED_SUBJECT = "Appointment Booked!";

    public static final String APPOINTMENT_BOOKED_BODY = "You have successfully booked an appointment" +
            " with vet %s %s for your pet, the %s %s, on %s at %s.\n" +
            "Please arrive a few minutes early and bring any necessary documents or medical records if applicable.\n" +
            "\n" +
            "Best regards,  \n" +
            "The Pet Med Team  \n" +
            "http://localhost:8080/  \n";
}
