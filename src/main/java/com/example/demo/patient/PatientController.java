package com.example.demo.patient;

import com.example.demo.constants.ApiConstants;
import com.example.demo.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(ApiConstants.Paths.API_PATIENTS)
@RequiredArgsConstructor
public class PatientController {

    private final PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        // Blood sugar readings are typically added via their own endpoint

        Patient savedPatient = patientRepository.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(PatientDTO.fromEntity(savedPatient));
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patientDTOs = patientRepository.findAll().stream()
                .map(PatientDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(patientDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.PATIENT, ApiConstants.ID, id));
        return ResponseEntity.ok(PatientDTO.fromEntity(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        // Check if the ID in the path matches the ID in the request body
        if (!id.equals(patientDTO.getId())) {
            throw new IllegalArgumentException(ApiConstants.ExceptionMessage.ID_MISMATCH);
        }
        
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.PATIENT, ApiConstants.ID, id));
        
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        // Blood sugar readings are managed via their dedicated controller
        Patient updatedPatient = patientRepository.save(patient);
        return ResponseEntity.ok(PatientDTO.fromEntity(updatedPatient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.PATIENT, ApiConstants.ID, id));
        patientRepository.delete(patient);
        return ResponseEntity.noContent().build();
    }

}
