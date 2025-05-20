package com.example.demo.patient;

import com.example.demo.constants.ApiConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDTO {
    private Long id;
    
    @NotBlank(message = ApiConstants.Validation.FIRST_NAME_REQUIRED)
    @Size(min = 1, max = 100, message = ApiConstants.Validation.FIRST_NAME_SIZE)
    private String firstName;
    
    @NotBlank(message = ApiConstants.Validation.LAST_NAME_REQUIRED)
    @Size(min = 1, max = 100, message = ApiConstants.Validation.LAST_NAME_SIZE)
    private String lastName;
    
    @NotNull(message = ApiConstants.Validation.DOB_REQUIRED)
    @PastOrPresent(message = ApiConstants.Validation.DOB_PAST_OR_PRESENT)
    private LocalDate dateOfBirth;
    
    private List<BloodSugarReadingDTO> bloodSugarReadings;

    // Lombok handles constructors via @NoArgsConstructor, @AllArgsConstructor, and @Builder

    // Static factory method to convert from Entity to DTO
    public static PatientDTO fromEntity(Patient patient) {
        if (patient == null) {
            return null;
        }
        List<BloodSugarReadingDTO> readingDTOs = patient.getBloodSugarReadings() != null ? 
            patient.getBloodSugarReadings().stream()
                   .map(BloodSugarReadingDTO::fromEntity) // Assumes BloodSugarReadingDTO will also have fromEntity
                   .collect(java.util.stream.Collectors.toList()) :
            java.util.Collections.emptyList();
            
        return new PatientDTO(
            patient.getId(),
            patient.getFirstName(),
            patient.getLastName(),
            patient.getDateOfBirth(),
            readingDTOs
        );
    }

    // Lombok handles getters, setters, equals, hashCode, and toString via @Data
}
