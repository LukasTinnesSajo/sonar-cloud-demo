package com.example.demo.patient;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Patient patient = new Patient();
        LocalDate dob = LocalDate.of(1990, 1, 1);
        
        // Act
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(dob);
        
        // Assert
        assertEquals(1L, patient.getId());
        assertEquals("John", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
        assertEquals(dob, patient.getDateOfBirth());
        assertNotNull(patient.getBloodSugarReadings());
        assertTrue(patient.getBloodSugarReadings().isEmpty());
    }

    @Test
    void testBuilder() {
        // Arrange & Act
        LocalDate dob = LocalDate.of(1990, 1, 1);
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(dob)
                .bloodSugarReadings(Collections.emptyList())
                .build();
        
        // Assert
        assertEquals(1L, patient.getId());
        assertEquals("John", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
        assertEquals(dob, patient.getDateOfBirth());
        assertNotNull(patient.getBloodSugarReadings());
        assertTrue(patient.getBloodSugarReadings().isEmpty());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        LocalDate dob = LocalDate.of(1990, 1, 1);
        
        // Act
        Patient patient = new Patient("John", "Doe", dob);
        
        // Assert
        assertNull(patient.getId());
        assertEquals("John", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
        assertEquals(dob, patient.getDateOfBirth());
        assertNotNull(patient.getBloodSugarReadings());
        assertTrue(patient.getBloodSugarReadings().isEmpty());
    }
}
