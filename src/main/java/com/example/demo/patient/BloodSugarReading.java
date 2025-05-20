package com.example.demo.patient;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "blood_sugar_readings")
public class BloodSugarReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private double level; // e.g., in mg/dL or mmol/L

    @Column
    private String unit = "mg/dL"; // Default unit, can be specified

    // Constructors
    public BloodSugarReading() {
    }

    public BloodSugarReading(LocalDateTime timestamp, double level, String unit) {
        this.timestamp = timestamp;
        this.level = level;
        this.unit = unit;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodSugarReading that = (BloodSugarReading) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BloodSugarReading{" +
               "id=" + id +
               ", timestamp=" + timestamp +
               ", level=" + level +
               ", unit='" + unit + '\'' +
               '}';
    }
}
