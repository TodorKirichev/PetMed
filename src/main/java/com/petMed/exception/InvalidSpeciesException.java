package com.petMed.exception;

public class InvalidSpeciesException extends RuntimeException {
    public InvalidSpeciesException(String message) {
        super(message);
    }
}
