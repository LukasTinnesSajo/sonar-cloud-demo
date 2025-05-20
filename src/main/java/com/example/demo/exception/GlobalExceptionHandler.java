package com.example.demo.exception;

import com.example.demo.constants.ApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, 
            HttpStatusCode status, WebRequest request) {
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ApiConstants.TIMESTAMP, LocalDateTime.now());
        body.put(ApiConstants.STATUS, status.value());
        body.put(ApiConstants.ERROR, ApiConstants.VALIDATION_FAILED);
        
        // Get all validation errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        
        body.put(ApiConstants.ERRORS, errors);
        
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ApiConstants.TIMESTAMP, LocalDateTime.now());
        body.put(ApiConstants.STATUS, HttpStatus.NOT_FOUND.value());
        body.put(ApiConstants.ERROR, ApiConstants.RESOURCE_NOT_FOUND);
        body.put(ApiConstants.MESSAGE, ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ApiConstants.TIMESTAMP, LocalDateTime.now());
        body.put(ApiConstants.STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ApiConstants.ERROR, ApiConstants.BAD_REQUEST);
        body.put(ApiConstants.MESSAGE, ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ApiConstants.TIMESTAMP, LocalDateTime.now());
        body.put(ApiConstants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(ApiConstants.ERROR, ApiConstants.INTERNAL_SERVER_ERROR);
        body.put(ApiConstants.MESSAGE, ApiConstants.UNEXPECTED_ERROR_OCCURRED);
        
        // Log the full stack trace only in debug mode
        if (logger.isDebugEnabled()) {
            logger.error("Unexpected error occurred: " + ex.getMessage(), ex);
        } else {
            logger.error("Unexpected error occurred: " + ex.getMessage());
        }
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
