package com.example.demo.patient;


public interface PatientReference {
    Long getId();
    String getFirstName();
    String getLastName();
    java.time.LocalDate getDateOfBirth();
}
