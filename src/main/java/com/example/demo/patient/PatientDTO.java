package com.example.demo.patient;

import java.time.LocalDate;
import java.util.List;

public class PatientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private List<BloodSugarReadingDTO> bloodSugarReadings;

    // Constructors
    public PatientDTO() {
    }

    public PatientDTO(Long id, String firstName, String lastName, LocalDate dateOfBirth, List<BloodSugarReadingDTO> bloodSugarReadings) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bloodSugarReadings = bloodSugarReadings;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<BloodSugarReadingDTO> getBloodSugarReadings() {
        return bloodSugarReadings;
    }

    public void setBloodSugarReadings(List<BloodSugarReadingDTO> bloodSugarReadings) {
        this.bloodSugarReadings = bloodSugarReadings;
    }
}
