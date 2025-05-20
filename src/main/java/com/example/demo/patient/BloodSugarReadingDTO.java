package com.example.demo.patient;

import java.time.LocalDateTime;

public class BloodSugarReadingDTO {
    private Long id;
    private LocalDateTime timestamp;
    private double level;
    private String unit;
    private Long patientId; // Only include patientId to avoid circular references in JSON if PatientDTO includes readings

    // Constructors
    public BloodSugarReadingDTO() {
    }

    public BloodSugarReadingDTO(Long id, LocalDateTime timestamp, double level, String unit, Long patientId) {
        this.id = id;
        this.timestamp = timestamp;
        this.level = level;
        this.unit = unit;
        this.patientId = patientId;
    }

    // Static factory method to convert from Entity to DTO
    public static BloodSugarReadingDTO fromEntity(BloodSugarReading reading) {
        if (reading == null) {
            return null;
        }
        Long patientId = (reading.getPatient() != null) ? reading.getPatient().getId() : null;
        return new BloodSugarReadingDTO(
            reading.getId(),
            reading.getTimestamp(),
            reading.getLevel(),
            reading.getUnit(),
            patientId
        );
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
