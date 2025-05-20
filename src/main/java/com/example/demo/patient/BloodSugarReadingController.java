package com.example.demo.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/patients/{patientId}/readings")
public class BloodSugarReadingController {

    private final BloodSugarReadingRepository bloodSugarReadingRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public BloodSugarReadingController(BloodSugarReadingRepository bloodSugarReadingRepository, PatientRepository patientRepository) {
        this.bloodSugarReadingRepository = bloodSugarReadingRepository;
        this.patientRepository = patientRepository;
    }

    @PostMapping
    public ResponseEntity<?> createReading(@PathVariable Long patientId, @RequestBody BloodSugarReadingDTO readingDTO) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
        
        BloodSugarReading reading = new BloodSugarReading();
        reading.setPatient(patient);
        reading.setTimestamp(readingDTO.getTimestamp());
        reading.setLevel(readingDTO.getLevel());
        reading.setUnit(readingDTO.getUnit());
        BloodSugarReading savedReading = bloodSugarReadingRepository.save(reading);
        return new ResponseEntity<>(BloodSugarReadingDTO.fromEntity(savedReading), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllReadingsForPatient(@PathVariable Long patientId) {
        // Ensure patient exists
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        List<BloodSugarReadingDTO> readingDTOs = bloodSugarReadingRepository.findByPatientId(patientId).stream()
                .map(BloodSugarReadingDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(readingDTOs, HttpStatus.OK);
    }

    @GetMapping("/{readingId}")
    public ResponseEntity<Object> getReadingById(@PathVariable Long patientId, @PathVariable Long readingId) {
        // Ensure patient exists
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        BloodSugarReading reading = bloodSugarReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException("BloodSugarReading", "id", readingId));

        if (!reading.getPatient().getId().equals(patientId)) {
            return new ResponseEntity<>("Reading does not belong to the specified patient.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(BloodSugarReadingDTO.fromEntity(reading), HttpStatus.OK);
    }

    @PutMapping("/{readingId}")
    public ResponseEntity<Object> updateReading(@PathVariable Long patientId, @PathVariable Long readingId, @RequestBody BloodSugarReadingDTO readingDetailsDTO) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        BloodSugarReading reading = bloodSugarReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException("BloodSugarReading", "id", readingId));

        if (!reading.getPatient().getId().equals(patient.getId())) {
            return new ResponseEntity<>("Reading does not belong to the specified patient.", HttpStatus.BAD_REQUEST);
        }

        reading.setTimestamp(readingDetailsDTO.getTimestamp());
        reading.setLevel(readingDetailsDTO.getLevel());
        reading.setUnit(readingDetailsDTO.getUnit());
        BloodSugarReading updatedReading = bloodSugarReadingRepository.save(reading);
        return new ResponseEntity<>(BloodSugarReadingDTO.fromEntity(updatedReading), HttpStatus.OK);
    }

    @DeleteMapping("/{readingId}")
    public ResponseEntity<Object> deleteReading(@PathVariable Long patientId, @PathVariable Long readingId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        BloodSugarReading reading = bloodSugarReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException("BloodSugarReading", "id", readingId));

        if (!reading.getPatient().getId().equals(patient.getId())) {
            return new ResponseEntity<>("Reading does not belong to the specified patient.", HttpStatus.BAD_REQUEST);
        }

        bloodSugarReadingRepository.delete(reading);
        return ResponseEntity.ok().build(); // .<Object>build() is not strictly necessary when method returns ResponseEntity<Object>
    }

}
