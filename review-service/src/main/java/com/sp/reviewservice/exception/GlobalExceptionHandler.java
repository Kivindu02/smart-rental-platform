package com.sp.reviewservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyReviewedException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyReviewed(AlreadyReviewedException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "Already Reviewed",
                ex.getMessage()
        );
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePropertyNotFound(PropertyNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Property not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                fieldError -> fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                fieldErrors
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message,
            Object data
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);

        if(data != null) {
            body.put("data", data);
        }

        return ResponseEntity.status(status).body(body);
    }
}
