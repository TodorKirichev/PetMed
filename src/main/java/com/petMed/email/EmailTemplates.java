package com.petMed.email;

public class EmailTemplates {

    public static final String CONFIRM_EMAIL_SUBJECT = "Welcome to Pet Med!";

    public static final String CONFIRM_EMAIL_BODY = "Hi %s %s,\n" +
            "\n" +
            "Thank you for signing up for Pet Med!\n" +
            "\n" +
            "If you did not create this account, please ignore this email.\n" +
            "\n" +
            "Best regards,  \n" +
            "The Pet Med Team  \n" +
            "http://localhost:8080/  \n";
}
