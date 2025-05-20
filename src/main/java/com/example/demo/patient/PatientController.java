package com.example.demo.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
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
                .collect(Collectors.toList());
        return ResponseEntity.ok(patientDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        return ResponseEntity.ok(PatientDTO.fromEntity(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDetailsDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        
        patient.setFirstName(patientDetailsDTO.getFirstName());
        patient.setLastName(patientDetailsDTO.getLastName());
        patient.setDateOfBirth(patientDetailsDTO.getDateOfBirth());
        // Blood sugar readings are managed via their dedicated controller
        Patient updatedPatient = patientRepository.save(patient);
        return ResponseEntity.ok(PatientDTO.fromEntity(updatedPatient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        patientRepository.delete(patient);
        return ResponseEntity.ok().<Void>build();
    }

}
