package com.example.demo.patient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BloodSugarReadingDTOTest {

    @Test
    void fromEntity_withNull_shouldReturnNull() {
        assertNull(BloodSugarReadingDTO.fromEntity(null));
    }

    @Test
    void fromEntity_withReading_shouldMapCorrectly() {
        // Given
        Patient patient = new Patient();
        patient.setId(1L);

        BloodSugarReading reading = new BloodSugarReading();
        reading.setId(1L);
        reading.setTimestamp(LocalDateTime.of(2023, 1, 1, 12, 0));
        reading.setLevel(100.0);
        reading.setUnit("mg/dL");
        reading.setPatient(patient);

        // When
        BloodSugarReadingDTO dto = BloodSugarReadingDTO.fromEntity(reading);

        // Then
        assertNotNull(dto);
        assertEquals(reading.getId(), dto.getId());
        assertEquals(reading.getTimestamp(), dto.getTimestamp());
        assertEquals(reading.getLevel(), dto.getLevel(), 0.001);
        assertEquals(reading.getUnit(), dto.getUnit());
        assertEquals(patient.getId(), dto.getPatientId());
    }

    @Test
    void fromEntity_withNullPatient_shouldSetPatientIdToNull() {
        // Given
        BloodSugarReading reading = new BloodSugarReading();
        reading.setId(1L);
        reading.setPatient(null);

        // When
        BloodSugarReadingDTO dto = BloodSugarReadingDTO.fromEntity(reading);


        // Then
        assertNull(dto.getPatientId());
    }


    @Test
    void builder_shouldCreateInstance() {
        // Given & When
        LocalDateTime now = LocalDateTime.now();
        BloodSugarReadingDTO dto = BloodSugarReadingDTO.builder()
                .id(1L)
                .timestamp(now)
                .level(100.0)
                .unit("mg/dL")
                .patientId(1L)
                .build();

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(now, dto.getTimestamp());
        assertEquals(100.0, dto.getLevel(), 0.001);
        assertEquals("mg/dL", dto.getUnit());
        assertEquals(1L, dto.getPatientId());
    }

    @Test
    void equalsAndHashCode() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BloodSugarReadingDTO dto1 = BloodSugarReadingDTO.builder()
                .id(1L)
                .timestamp(now)
                .level(100.0)
                .unit("mg/dL")
                .patientId(1L)
                .build();

        BloodSugarReadingDTO dto2 = BloodSugarReadingDTO.builder()
                .id(1L)
                .timestamp(now)
                .level(100.0)
                .unit("mg/dL")
                .patientId(1L)
                .build();

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
