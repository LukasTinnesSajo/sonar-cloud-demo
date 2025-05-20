package com.example.demo.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        return new ResponseEntity<>(toPatientDTO(savedPatient), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patientDTOs = patientRepository.findAll().stream()
                .map(this::toPatientDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patientDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(patient -> ResponseEntity.ok(toPatientDTO(patient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDetailsDTO) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setFirstName(patientDetailsDTO.getFirstName());
                    patient.setLastName(patientDetailsDTO.getLastName());
                    patient.setDateOfBirth(patientDetailsDTO.getDateOfBirth());
                    // Blood sugar readings are managed via their dedicated controller
                    Patient updatedPatient = patientRepository.save(patient);
                    return ResponseEntity.ok(toPatientDTO(updatedPatient));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patientRepository.delete(patient);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Helper method to convert Patient Entity to PatientDTO
    private PatientDTO toPatientDTO(Patient patient) {
        List<BloodSugarReadingDTO> readingDTOs = patient.getBloodSugarReadings().stream()
                .map(this::toBloodSugarReadingDTO)
                .collect(Collectors.toList());
        return new PatientDTO(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth(), readingDTOs);
    }

    // Helper method to convert BloodSugarReading Entity to BloodSugarReadingDTO
    private BloodSugarReadingDTO toBloodSugarReadingDTO(BloodSugarReading reading) {
        return new BloodSugarReadingDTO(reading.getId(), reading.getTimestamp(), reading.getLevel(), reading.getUnit(), reading.getPatient().getId());
    }
}
