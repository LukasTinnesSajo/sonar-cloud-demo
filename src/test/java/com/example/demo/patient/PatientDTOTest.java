package com.example.demo.patient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatientDTOTest {

    @Test
    void fromEntity_withNull_shouldReturnNull() {
        assertNull(PatientDTO.fromEntity(null));
    }

    @Test
    void fromEntity_withPatient_shouldMapCorrectly() {
        // Given
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setBloodSugarReadings(Collections.emptyList());

        // When
        PatientDTO dto = PatientDTO.fromEntity(patient);

        // Then
        assertNotNull(dto);
        assertEquals(patient.getId(), dto.getId());
        assertEquals(patient.getFirstName(), dto.getFirstName());
        assertEquals(patient.getLastName(), dto.getLastName());
        assertEquals(patient.getDateOfBirth(), dto.getDateOfBirth());
        assertNotNull(dto.getBloodSugarReadings());
        assertTrue(dto.getBloodSugarReadings().isEmpty());
    }

    @Test
    void fromEntity_withNullReadings_shouldReturnEmptyList() {
        // Given
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setBloodSugarReadings(null);

        // When
        PatientDTO dto = PatientDTO.fromEntity(patient);


        // Then
        assertNotNull(dto.getBloodSugarReadings());
        assertTrue(dto.getBloodSugarReadings().isEmpty());
    }


    @Test
    void builder_shouldCreateInstance() {
        // Given & When
        PatientDTO dto = PatientDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .bloodSugarReadings(Collections.emptyList())
                .build();

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getDateOfBirth());
        assertNotNull(dto.getBloodSugarReadings());
        assertTrue(dto.getBloodSugarReadings().isEmpty());
    }

    @Test
    void equalsAndHashCode() {
        // Given
        PatientDTO dto1 = PatientDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        PatientDTO dto2 = PatientDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
