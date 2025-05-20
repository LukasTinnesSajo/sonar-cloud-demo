package com.example.demo.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodSugarReadingRepository extends JpaRepository<BloodSugarReading, Long> {
    List<BloodSugarReading> findByPatientId(Long patientId);
    List<BloodSugarReading> findByPatientIdAndTimestampBetween(Long patientId, java.time.LocalDateTime start, java.time.LocalDateTime end);
}
