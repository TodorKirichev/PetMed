package com.petMed.web;

import com.petMed.exception.AppointmentNotFoundException;
import com.petMed.exception.PetNotFoundException;
import com.petMed.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            UserNotFoundException.class,
            PetNotFoundException.class,
            AppointmentNotFoundException.class,
            NoResourceFoundException.class
    })
    public ModelAndView handleNotFoundException() {
        return new ModelAndView("not-found");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
            AccessDeniedException.class,
            AuthorizationDeniedException.class
    })
    public ModelAndView handleAccessDeniedException() {
        return new ModelAndView("forbidden");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleOtherExceptions() {
        return new ModelAndView("server-error");
    }
}
