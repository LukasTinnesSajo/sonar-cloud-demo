package com.example.medical;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MedicalReadingUtilsTest {

    @Test
    void testCalculateAdjustedReading_daytime() {
        LocalDateTime time = LocalDateTime.of(2025, 5, 31, 14, 0); // 2 PM
        double result = MedicalReadingUtils.calculateAdjustedReading(time, 100.0);
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testCalculateAdjustedReading_nighttime() {
        LocalDateTime time = LocalDateTime.of(2025, 5, 31, 23, 0); // 11 PM
        double result = MedicalReadingUtils.calculateAdjustedReading(time, 100.0);
        assertEquals(110.0, result, 0.001);
    }

    @Test
    void testCalculateDifference() {
        assertEquals(20.0, MedicalReadingUtils.calculateDifference(80.0, 100.0), 0.001);
    }

    @Test
    void testCalculateDifferenceDuplicate() {
        assertEquals(-10.0, MedicalReadingUtils.calculateDifferenceDuplicate(50.0, 40.0), 0.001);
    }

    @Test
    void testCalculateTimeDifferenceMinutes_positive() {
        LocalDateTime t1 = LocalDateTime.of(2025, 5, 31, 14, 0);
        LocalDateTime t2 = LocalDateTime.of(2025, 5, 31, 15, 0);
        assertEquals(60, MedicalReadingUtils.calculateTimeDifferenceMinutes(t1, t2));
    }

    @Test
    void testCalculateTimeDifferenceMinutes_negative() {
        LocalDateTime t1 = LocalDateTime.of(2025, 5, 31, 16, 0);
        LocalDateTime t2 = LocalDateTime.of(2025, 5, 31, 15, 0);
        assertEquals(-60, MedicalReadingUtils.calculateTimeDifferenceMinutes(t1, t2));
    }
}
