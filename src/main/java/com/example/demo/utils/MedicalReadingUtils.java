package com.example.demo.utils;

import java.time.LocalDateTime;
import java.time.Duration;

public final class MedicalReadingUtils {


    private MedicalReadingUtils() {
        // Prevent instantiation
    }

    public static double calculateAdjustedReading(LocalDateTime readingTime, double rawValue) {
        int hour = readingTime.getHour();
        double adjustment = (hour >= 22 || hour < 6) ? 1.10 : 1.00;
        return rawValue * adjustment;
    }


    public static double calculateDifference(double firstValue, double secondValue) {
        return secondValue - firstValue;
    }


    public static long calculateTimeDifferenceMinutes(LocalDateTime firstTime, LocalDateTime secondTime) {
        return Duration.between(firstTime, secondTime).toMinutes();
    }
}
