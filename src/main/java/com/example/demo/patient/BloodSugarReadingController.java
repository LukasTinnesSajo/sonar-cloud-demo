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
@RequestMapping(ApiConstants.Paths.API_PATIENTS_READINGS)
@RequiredArgsConstructor
public class BloodSugarReadingController {

    private final BloodSugarReadingRepository bloodSugarReadingRepository;
    private final PatientRepository patientRepository;



    @PostMapping
    public ResponseEntity<BloodSugarReadingDTO> createReading(
            @PathVariable Long patientId, 
            @Valid @RequestBody BloodSugarReadingDTO readingDTO) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.PATIENT, ApiConstants.ID, patientId));
        
        BloodSugarReading reading = new BloodSugarReading();
        reading.setPatient(patient);
        reading.setTimestamp(readingDTO.getTimestamp());
        reading.setLevel(readingDTO.getLevel());
        reading.setUnit(readingDTO.getUnit());
        BloodSugarReading savedReading = bloodSugarReadingRepository.save(reading);
        return new ResponseEntity<>(BloodSugarReadingDTO.fromEntity(savedReading), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BloodSugarReadingDTO>> getAllReadingsForPatient(@PathVariable Long patientId) {
        // Ensure patient exists
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.PATIENT, ApiConstants.ID, patientId));

        List<BloodSugarReadingDTO> readingDTOs = bloodSugarReadingRepository.findByPatientId(patientId).stream()
                .map(BloodSugarReadingDTO::fromEntity)
                .toList();
        return new ResponseEntity<>(readingDTOs, HttpStatus.OK);
    }

    @GetMapping("/{readingId}")
    public ResponseEntity<BloodSugarReadingDTO> getReadingById(@PathVariable Long patientId, @PathVariable Long readingId) {
        // Ensure patient exists
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.PATIENT, ApiConstants.ID, patientId));

        BloodSugarReading reading = bloodSugarReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.BLOOD_SUGAR_READING, ApiConstants.ID, readingId));

        if (!reading.getPatient().getId().equals(patientId)) {
            throw new IllegalArgumentException(ApiConstants.ExceptionMessage.READING_NOT_BELONG);
        }
        return new ResponseEntity<>(BloodSugarReadingDTO.fromEntity(reading), HttpStatus.OK);
    }

    @PutMapping("/{readingId}")
    public ResponseEntity<BloodSugarReadingDTO> updateReading(
            @PathVariable Long patientId,
            @PathVariable Long readingId,
            @Valid @RequestBody BloodSugarReadingDTO readingDetailsDTO) {
        // Check if the readingId in the path matches the ID in the request body
        if (!readingId.equals(readingDetailsDTO.getId())) {
            throw new IllegalArgumentException(ApiConstants.ExceptionMessage.ID_MISMATCH);
        }
        
        // Check if the patientId in the path matches the patientId in the request body
        if (!patientId.equals(readingDetailsDTO.getPatientId())) {
            throw new IllegalArgumentException(ApiConstants.ExceptionMessage.PATIENT_ID_MISMATCH);
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.PATIENT, ApiConstants.ID, patientId));

        BloodSugarReading reading = bloodSugarReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.BLOOD_SUGAR_READING, ApiConstants.ID, readingId));

        if (!reading.getPatient().getId().equals(patient.getId())) {
            throw new IllegalArgumentException(ApiConstants.ExceptionMessage.READING_NOT_BELONG);
        }

        reading.setTimestamp(readingDetailsDTO.getTimestamp());
        reading.setLevel(readingDetailsDTO.getLevel());
        reading.setUnit(readingDetailsDTO.getUnit());
        BloodSugarReading updatedReading = bloodSugarReadingRepository.save(reading);
        return new ResponseEntity<>(BloodSugarReadingDTO.fromEntity(updatedReading), HttpStatus.OK);
    }

    @DeleteMapping("/{readingId}")
    public ResponseEntity<Void> deleteReading(@PathVariable Long patientId, @PathVariable Long readingId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.PATIENT, ApiConstants.ID, patientId));

        BloodSugarReading reading = bloodSugarReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstants.BLOOD_SUGAR_READING, ApiConstants.ID, readingId));

        if (!reading.getPatient().getId().equals(patient.getId())) {
            throw new IllegalArgumentException(ApiConstants.ExceptionMessage.READING_NOT_BELONG);
        }

        bloodSugarReadingRepository.delete(reading);
        return ResponseEntity.noContent().build();
    }

}
