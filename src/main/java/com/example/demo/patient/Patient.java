package com.example.demo.patient;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private java.time.LocalDate dateOfBirth;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BloodSugarReading> bloodSugarReadings = new ArrayList<>();

    // Constructors
    public Patient() {
    }

    public Patient(String firstName, String lastName, java.time.LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public java.time.LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(java.time.LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<BloodSugarReading> getBloodSugarReadings() {
        return bloodSugarReadings;
    }

    public void setBloodSugarReadings(List<BloodSugarReading> bloodSugarReadings) {
        this.bloodSugarReadings = bloodSugarReadings;
    }

    public void addBloodSugarReading(BloodSugarReading reading) {
        bloodSugarReadings.add(reading);
        reading.setPatient(this);
    }

    public void removeBloodSugarReading(BloodSugarReading reading) {
        bloodSugarReadings.remove(reading);
        reading.setPatient(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Patient{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", dateOfBirth=" + dateOfBirth +
               '}';
    }
}
