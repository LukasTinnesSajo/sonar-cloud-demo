package com.example.demo.constants;

public final class ApiConstants {
    // Common
    public static final String ID = "id";
    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String ERRORS = "errors";
    public static final String MESSAGE = "message";
    
    // Error Messages
    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String RESOURCE_NOT_FOUND = "Resource Not Found";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred";
    
    // Resource Names
    public static final String PATIENT = "Patient";
    public static final String BLOOD_SUGAR_READING = "BloodSugarReading";
    
    // Validation Messages
    public static class Validation {
        // Patient
        public static final String FIRST_NAME_REQUIRED = "First name is required";
        public static final String FIRST_NAME_SIZE = "First name must be between 1 and 100 characters";
        public static final String LAST_NAME_REQUIRED = "Last name is required";
        public static final String LAST_NAME_SIZE = "Last name must be between 1 and 100 characters";
        public static final String DOB_REQUIRED = "Date of birth is required";
        public static final String DOB_PAST_OR_PRESENT = "Date of birth must be in the past or present";
        
        // Blood Sugar Reading
        public static final String TIMESTAMP_REQUIRED = "Timestamp is required";
        public static final String TIMESTAMP_PAST_OR_PRESENT = "Timestamp must be in the past or present";
        public static final String LEVEL_REQUIRED = "Level is required";
        public static final String LEVEL_POSITIVE = "Level must be a positive number";
        public static final String LEVEL_MAX = "Level must be less than or equal to 1000";
        public static final String UNIT_REQUIRED = "Unit is required";
        public static final String UNIT_PATTERN = "Unit must be either 'mg/dL' or 'mmol/L'";
        public static final String PATIENT_ID_REQUIRED = "Patient ID is required";
    }
    
    // Exception Messages
    public static class ExceptionMessage {
        public static final String ID_MISMATCH = "ID in path does not match ID in request body";
        public static final String PATIENT_ID_MISMATCH = "patientId in path does not match patientId in request body";
        public static final String READING_NOT_BELONG = "Reading does not belong to the specified patient";
    }
    
    // API Paths
    public static class Paths {
        public static final String API_PATIENTS = "/api/patients";
        public static final String API_PATIENTS_READINGS = "/api/patients/{patientId}/readings";
    }
    
    // Table Names
    public static class Tables {
        public static final String PATIENTS = "patients";
        public static final String BLOOD_SUGAR_READINGS = "blood_sugar_readings";
    }
    
    // Column Names
    public static class Columns {
        public static final String PATIENT_ID = "patient_id";
    }
    
    // Units
    public static class Units {
        public static final String MG_DL = "mg/dL";
        public static final String MMOL_L = "mmol/L";
    }
    
    private ApiConstants() {
        // Private constructor to prevent instantiation
    }
}
