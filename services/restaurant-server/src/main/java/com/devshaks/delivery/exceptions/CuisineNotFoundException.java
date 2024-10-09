package com.devshaks.delivery.exceptions;

public class CuisineNotFoundException extends RuntimeException {
    public CuisineNotFoundException(String message) {
        super(message);
    }
}
