package com.devshaks.delivery.customer.handlers;

import com.devshaks.delivery.customer.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
// Global Exception Handler to handle different types of exceptions in a centralized manner
public class GlobalExceptionHandlers {

    // Handle CustomerNotFoundException specifically
    // This method is triggered when a CustomerNotFoundException is thrown
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleException(CustomerNotFoundException e) {
        // Return a 404 Not Found status with the exception message in the response body
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    // Handle validation errors when the request body doesn't meet the validation constraints
    // This method is triggered when a MethodArgumentNotValidException is thrown (usually during @Valid validation failures)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        // Create a HashMap to store field-specific validation errors
        var errors = new HashMap<String, String>();

        // Iterate through the validation errors, extract the field name and error message, and add to the map
        e.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError)error).getField();  // Get the name of the field that failed validation
                    var errorMessage = error.getDefaultMessage();     // Get the validation error message
                    errors.put(fieldName, errorMessage);              // Put the field name and error message in the map
                });

        // Return a 400 Bad Request status with the errors wrapped in a custom ErrorResponse object
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }
}
