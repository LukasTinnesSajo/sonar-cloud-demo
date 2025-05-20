package com.example.demo.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // JpaRepository provides findAll, findById, save, deleteById, etc.
    // We can add custom query methods here if needed later
}
