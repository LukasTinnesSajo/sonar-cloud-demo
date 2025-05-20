package com.example.demo.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        return patientRepository.findById(patientId)
                .map(patient -> {
                    BloodSugarReading reading = new BloodSugarReading();
                    reading.setPatient(patient);
                    reading.setTimestamp(readingDTO.getTimestamp());
                    reading.setLevel(readingDTO.getLevel());
                    reading.setUnit(readingDTO.getUnit());
                    BloodSugarReading savedReading = bloodSugarReadingRepository.save(reading);
                    return new ResponseEntity<Object>(toBloodSugarReadingDTO(savedReading), HttpStatus.CREATED);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found with id: " + patientId));
    }

    @GetMapping
    public ResponseEntity<?> getAllReadingsForPatient(@PathVariable Long patientId) {
         if (!patientRepository.existsById(patientId)) {
            return new ResponseEntity<Object>("Patient not found with id: " + patientId, HttpStatus.NOT_FOUND);
        }
        List<BloodSugarReadingDTO> readingDTOs = bloodSugarReadingRepository.findByPatientId(patientId).stream()
                .map(this::toBloodSugarReadingDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(readingDTOs, HttpStatus.OK);
    }

    @GetMapping("/{readingId}")
    public ResponseEntity<?> getReadingById(@PathVariable Long patientId, @PathVariable Long readingId) {
        return bloodSugarReadingRepository.findById(readingId)
                .map(reading -> {
                    if (!reading.getPatient().getId().equals(patientId)) {
                        return new ResponseEntity<Object>("Reading does not belong to the specified patient.", HttpStatus.BAD_REQUEST);
                    }
                    return new ResponseEntity<Object>(toBloodSugarReadingDTO(reading), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<Object>("Reading not found with id: " + readingId, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{readingId}")
    public ResponseEntity<?> updateReading(@PathVariable Long patientId, @PathVariable Long readingId, @RequestBody BloodSugarReadingDTO readingDetailsDTO) {
        if (!patientRepository.existsById(patientId)) {
             return new ResponseEntity<Object>("Patient not found with id: " + patientId, HttpStatus.NOT_FOUND);
        }
        return bloodSugarReadingRepository.findById(readingId)
                .map(reading -> {
                    if (!reading.getPatient().getId().equals(patientId)) {
                        return new ResponseEntity<Object>("Reading does not belong to the specified patient.", HttpStatus.BAD_REQUEST);
                    }
                    reading.setTimestamp(readingDetailsDTO.getTimestamp());
                    reading.setLevel(readingDetailsDTO.getLevel());
                    reading.setUnit(readingDetailsDTO.getUnit());
                    BloodSugarReading updatedReading = bloodSugarReadingRepository.save(reading);
                    return new ResponseEntity<Object>(toBloodSugarReadingDTO(updatedReading), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<Object>("Reading not found with id: " + readingId, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{readingId}")
    public ResponseEntity<?> deleteReading(@PathVariable Long patientId, @PathVariable Long readingId) {
         if (!patientRepository.existsById(patientId)) {
            return new ResponseEntity<Object>("Patient not found with id: " + patientId, HttpStatus.NOT_FOUND);
        }
        return bloodSugarReadingRepository.findById(readingId)
                .map(reading -> {
                     if (!reading.getPatient().getId().equals(patientId)) {
                        return new ResponseEntity<Object>("Reading does not belong to the specified patient.", HttpStatus.BAD_REQUEST);
                    }
                    bloodSugarReadingRepository.delete(reading);
                    return ResponseEntity.ok().<Object>build(); // Return type compatible with Object for consistency
                })
                .orElse(new ResponseEntity<Object>("Reading not found with id: " + readingId, HttpStatus.NOT_FOUND));
    }

    // Helper method to convert BloodSugarReading Entity to BloodSugarReadingDTO
    private BloodSugarReadingDTO toBloodSugarReadingDTO(BloodSugarReading reading) {
        return new BloodSugarReadingDTO(reading.getId(), reading.getTimestamp(), reading.getLevel(), reading.getUnit(), reading.getPatient().getId());
    }
}
