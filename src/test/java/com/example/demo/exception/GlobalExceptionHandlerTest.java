package com.example.demo.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = Mockito.mock(WebRequest.class);
    }

    @Test
    void handleMethodArgumentNotValid_shouldReturnValidationErrors() {
        // Given
        FieldError fieldError1 = new FieldError("objectName", "field1", "must not be null");
        FieldError fieldError2 = new FieldError("objectName", "field2", "size must be between 1 and 50");
        
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
        
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // When
        ResponseEntity<Object> response = globalExceptionHandler.handleMethodArgumentNotValid(
                ex, headers, status, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> body = (java.util.Map<String, Object>) response.getBody();
        assertNotNull(body);
        
        assertEquals(status.value(), body.get("status"));
        assertEquals("Validation failed", body.get("error"));
        assertTrue(body.containsKey("timestamp"));
        
        @SuppressWarnings("unchecked")
        List<String> errors = (List<String>) body.get("errors");
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertTrue(errors.contains("must not be null"));
        assertTrue(errors.contains("size must be between 1 and 50"));
    }

    @Test
    void handleResourceNotFoundException_shouldReturnNotFoundResponse() {
        // Given
        String errorMessage = "Patient not found with id : '999'";
        ResourceNotFoundException ex = new ResourceNotFoundException("Patient", "id", 999L);

        // When
        ResponseEntity<Object> response = globalExceptionHandler.handleResourceNotFoundException(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> body = (java.util.Map<String, Object>) response.getBody();
        assertNotNull(body);
        
        assertEquals(HttpStatus.NOT_FOUND.value(), body.get("status"));
        assertEquals("Resource Not Found", body.get("error"));
        assertEquals(errorMessage, body.get("message"));
        assertTrue(body.containsKey("timestamp"));
    }

    @Test
    void handleIllegalArgumentException_shouldReturnBadRequestResponse() {
        // Given
        String errorMessage = "Invalid argument provided";
        IllegalArgumentException ex = new IllegalArgumentException(errorMessage);

        // When
        ResponseEntity<Object> response = globalExceptionHandler.handleIllegalArgumentException(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> body = (java.util.Map<String, Object>) response.getBody();
        assertNotNull(body);
        
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertEquals("Bad Request", body.get("error"));
        assertEquals(errorMessage, body.get("message"));
        assertTrue(body.containsKey("timestamp"));
    }

    @Test
    void handleAllExceptions_shouldReturnInternalServerErrorResponse() {
        // Given
        String errorMessage = "Unexpected error occurred";
        Exception ex = new Exception(errorMessage);

        // When
        ResponseEntity<Object> response = globalExceptionHandler.handleAllExceptions(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("An unexpected error occurred", body.get("message"));
        assertTrue(body.containsKey("timestamp"));
    }
}
