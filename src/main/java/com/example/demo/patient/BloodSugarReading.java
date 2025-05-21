package com.example.demo.patient;

import com.example.demo.constants.ApiConstants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = ApiConstants.Tables.BLOOD_SUGAR_READINGS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BloodSugarReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Patient.class)
    @JoinColumn(name = ApiConstants.Columns.PATIENT_ID, nullable = false)
    @JsonBackReference
    private PatientReference patient;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private double level; // e.g., in mg/dL or mmol/L

    @Column
    private String unit = ApiConstants.Units.MG_DL; // Default unit, can be specified

    @Builder
    public BloodSugarReading(LocalDateTime timestamp, double level, String unit) {
        this.timestamp = timestamp;
        this.level = level;
        this.unit = unit;
    }

    // Lombok handles getters and setters via @Getter and @Setter

    // Lombok handles equals, hashCode, and toString via @EqualsAndHashCode and @ToString
}
