package com.example.demo.patient;

import com.example.demo.constants.ApiConstants;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ApiConstants.Tables.PATIENTS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Patient implements PatientReference {

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

    @Builder
    public Patient(String firstName, String lastName, java.time.LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    // Lombok handles getters and setters via @Getter and @Setter

    public void addBloodSugarReading(BloodSugarReading reading) {
        bloodSugarReadings.add(reading);
        reading.setPatient(this);
    }

    public void removeBloodSugarReading(BloodSugarReading reading) {
        bloodSugarReadings.remove(reading);
        reading.setPatient(null);
    }

    // Lombok handles equals, hashCode, and toString via @EqualsAndHashCode and @ToString
}
