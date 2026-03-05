package com.bcda.Nourriture.exception;

public class RecetteNotFoundException extends RuntimeException {
    public RecetteNotFoundException(String message) {
        super(message);
    }

    public RecetteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
