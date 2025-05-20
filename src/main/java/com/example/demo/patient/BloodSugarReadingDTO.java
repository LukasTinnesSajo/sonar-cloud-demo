package com.example.demo.patient;

import com.example.demo.constants.ApiConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BloodSugarReadingDTO {
    private Long id;
    
    @NotNull(message = ApiConstants.Validation.TIMESTAMP_REQUIRED)
    @PastOrPresent(message = ApiConstants.Validation.TIMESTAMP_PAST_OR_PRESENT)
    private LocalDateTime timestamp;
    
    @NotNull(message = ApiConstants.Validation.LEVEL_REQUIRED)
    @DecimalMin(value = "0.0", message = ApiConstants.Validation.LEVEL_POSITIVE)
    @DecimalMax(value = "1000.0", message = ApiConstants.Validation.LEVEL_MAX)
    private Double level;
    
    @NotBlank(message = ApiConstants.Validation.UNIT_REQUIRED)
    @Pattern(regexp = "^(mg/dL|mmol/L)$", message = ApiConstants.Validation.UNIT_PATTERN)
    private String unit;
    
    @NotNull(message = ApiConstants.Validation.PATIENT_ID_REQUIRED)
    private Long patientId; // Only include patientId to avoid circular references in JSON if PatientDTO includes readings

    // Lombok handles constructors via @NoArgsConstructor, @AllArgsConstructor, and @Builder

    // Static factory method to convert from Entity to DTO
    public static BloodSugarReadingDTO fromEntity(BloodSugarReading reading) {
        if (reading == null) {
            return null;
        }
        Long patientId = (reading.getPatient() != null) ? reading.getPatient().getId() : null;
        return new BloodSugarReadingDTO(
            reading.getId(),
            reading.getTimestamp(),
            reading.getLevel(),
            reading.getUnit(),
            patientId
        );
    }

    // Lombok handles getters, setters, equals, hashCode, and toString via @Data
}
