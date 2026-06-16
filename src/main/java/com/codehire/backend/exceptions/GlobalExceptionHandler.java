package com.codehire.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 1. Tells Spring: "Listen to all Controllers for crashes"
public class GlobalExceptionHandler {

    // Catches explicitly malformed JSON (missing commas, braces, etc.)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJson(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", "Malformed JSON payload. Please check your syntax.");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Catches Validation Failures (e.g., blank code, negative IDs)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("error", "Validation Failed");

        // Extract the exact error message we wrote in the DTO
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        body.put("message", errorMessage);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // The Ultimate Safety Net: Catches literally any other Java exception we forgot about
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 500);
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected system error occurred.");

        // In a real enterprise app, you log 'ex.getMessage()' to a secure file right here,
        // but you NEVER send it back to the client.
        System.out.println("[CRITICAL ERROR CAUGHT] " + ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}