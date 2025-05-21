package com.example.demo.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// It's a good practice to make the custom handler have higher precedence than default ones
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        // You can create a custom error response object if needed
        // For simplicity, we're returning the exception message directly
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    // You can add more @ExceptionHandler methods here for other custom or built-in exceptions

    // Inner class for a structured error response (optional but good practice)
    private static class ApiError {
        private final HttpStatus status;
        private final String message;
        private final String debugMessage; // For more detailed error messages not exposed to the client

        public ApiError(HttpStatus status, String message, Throwable ex) {
            this.status = status;
            this.message = message;
            this.debugMessage = ex.getLocalizedMessage(); // Or ex.toString() or specific details
        }

        // Getters (and potentially setters if needed)
        public HttpStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getDebugMessage() {
            return debugMessage;
        }
    }
}
