package com.example.demo.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.example.demo.DemoApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class BloodSugarReadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BloodSugarReadingRepository bloodSugarReadingRepository;

    private ObjectMapper objectMapper;
    private Patient testPatient;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        bloodSugarReadingRepository.deleteAll(); // Clean readings first due to foreign key constraints
        patientRepository.deleteAll(); // Then clean patients

        // Create a persistent patient for use in tests
        testPatient = patientRepository.save(new Patient("Test", "Patient", LocalDate.of(1990, 1, 1)));
    }

    @Test
    void createReading_whenPatientExists_shouldReturnCreatedReading() throws Exception {
        BloodSugarReadingDTO readingDTO = new BloodSugarReadingDTO(null, LocalDateTime.now(), 120.5, "mg/dL", testPatient.getId());

        mockMvc.perform(post("/api/patients/{patientId}/readings", testPatient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.level", is(120.5)))
                .andExpect(jsonPath("$.patientId", is(testPatient.getId().intValue())));
    }

    @Test
    void createReading_whenPatientDoesNotExist_shouldReturnNotFound() throws Exception {
        BloodSugarReadingDTO readingDTO = new BloodSugarReadingDTO(null, LocalDateTime.now(), 120.5, "mg/dL", 999L);

        mockMvc.perform(post("/api/patients/999/readings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readingDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllReadingsForPatient_shouldReturnListOfReadings() throws Exception {
        BloodSugarReading reading1 = new BloodSugarReading(LocalDateTime.now().minusHours(1), 110.0, "mg/dL");
        reading1.setPatient(testPatient);
        bloodSugarReadingRepository.save(reading1);

        BloodSugarReading reading2 = new BloodSugarReading(LocalDateTime.now(), 115.0, "mg/dL");
        reading2.setPatient(testPatient);
        bloodSugarReadingRepository.save(reading2);

        mockMvc.perform(get("/api/patients/{patientId}/readings", testPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].level", is(110.0)))
                .andExpect(jsonPath("$[1].level", is(115.0)));
    }

    @Test
    void getReadingById_whenReadingExistsAndBelongsToPatient_shouldReturnReading() throws Exception {
        BloodSugarReading reading = new BloodSugarReading(LocalDateTime.now(), 130.0, "mg/dL");
        reading.setPatient(testPatient);
        BloodSugarReading savedReading = bloodSugarReadingRepository.save(reading);

        mockMvc.perform(get("/api/patients/{patientId}/readings/{readingId}", testPatient.getId(), savedReading.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedReading.getId().intValue())))
                .andExpect(jsonPath("$.level", is(130.0)));
    }

    @Test
    void getReadingById_whenReadingDoesNotExist_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/patients/{patientId}/readings/999", testPatient.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getReadingById_whenReadingDoesNotBelongToPatient_shouldReturnBadRequest() throws Exception {
        Patient otherPatient = patientRepository.save(new Patient("Other", "Patient", LocalDate.now()));
        BloodSugarReading reading = new BloodSugarReading(LocalDateTime.now(), 140.0, "mg/dL");
        reading.setPatient(otherPatient);
        BloodSugarReading savedReading = bloodSugarReadingRepository.save(reading);

        // Attempt to fetch reading using testPatient's ID but with a reading belonging to otherPatient
        mockMvc.perform(get("/api/patients/{patientId}/readings/{readingId}", testPatient.getId(), savedReading.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Reading does not belong to the specified patient")));
    }

    @Test
    void updateReading_whenExistsAndBelongs_shouldReturnUpdatedReading() throws Exception {
        BloodSugarReading existingReading = new BloodSugarReading(LocalDateTime.now().minusMinutes(30), 100.0, "mg/dL");
        existingReading.setPatient(testPatient);
        bloodSugarReadingRepository.save(existingReading);

        BloodSugarReadingDTO updatedDetails = new BloodSugarReadingDTO(existingReading.getId(), LocalDateTime.now(), 105.5, "mmol/L", testPatient.getId());

        mockMvc.perform(put("/api/patients/{patientId}/readings/{readingId}", testPatient.getId(), existingReading.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level", is(105.5)))
                .andExpect(jsonPath("$.unit", is("mmol/L")));
    }

    @Test
    void deleteReading_whenExistsAndBelongs_shouldReturnNoContent() throws Exception {
        BloodSugarReading reading = new BloodSugarReading(LocalDateTime.now(), 90.0, "mg/dL");
        reading.setPatient(testPatient);
        bloodSugarReadingRepository.save(reading);

        mockMvc.perform(delete("/api/patients/{patientId}/readings/{readingId}", testPatient.getId(), reading.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/patients/{patientId}/readings/{readingId}", testPatient.getId(), reading.getId()))
                .andExpect(status().isNotFound()); // Verify deleted
    }

    // Input validation tests
    @Test
    void createReading_withNegativeLevel_shouldReturnBadRequest() throws Exception {
        BloodSugarReadingDTO readingDTO = new BloodSugarReadingDTO(null, LocalDateTime.now(), -10.0, "mg/dL", testPatient.getId());

        mockMvc.perform(post("/api/patients/{patientId}/readings", testPatient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readingDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReading_withFutureTimestamp_shouldReturnBadRequest() throws Exception {
        LocalDateTime futureTime = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        BloodSugarReadingDTO readingDTO = new BloodSugarReadingDTO(null, futureTime, 120.0, "mg/dL", testPatient.getId());

        mockMvc.perform(post("/api/patients/{patientId}/readings", testPatient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readingDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReading_withEmptyUnit_shouldReturnBadRequest() throws Exception {
        BloodSugarReadingDTO readingDTO = new BloodSugarReadingDTO(null, LocalDateTime.now(), 120.0, "", testPatient.getId());

        mockMvc.perform(post("/api/patients/{patientId}/readings", testPatient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readingDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateReading_withIdMismatch_shouldReturnBadRequest() throws Exception {
        BloodSugarReading existingReading = new BloodSugarReading(LocalDateTime.now().minusHours(1), 100.0, "mg/dL");
        existingReading.setPatient(testPatient);
        BloodSugarReading savedReading = bloodSugarReadingRepository.save(existingReading);

        BloodSugarReadingDTO updatedReading = new BloodSugarReadingDTO(
            savedReading.getId() + 1, // Different ID
            LocalDateTime.now(),
            105.0,
            "mg/dL",
            testPatient.getId()
        );

        mockMvc.perform(put("/api/patients/{patientId}/readings/{readingId}", 
                          testPatient.getId(), savedReading.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedReading)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("ID in path does not match ID in request body")));
    }

    @Test
    void updateReading_withPatientIdMismatch_shouldReturnBadRequest() throws Exception {
        BloodSugarReading existingReading = new BloodSugarReading(LocalDateTime.now().minusHours(1), 100.0, "mg/dL");
        existingReading.setPatient(testPatient);
        BloodSugarReading savedReading = bloodSugarReadingRepository.save(existingReading);

        Patient otherPatient = patientRepository.save(new Patient("Other", "Patient", LocalDate.now()));
        
        BloodSugarReadingDTO updatedReading = new BloodSugarReadingDTO(
            savedReading.getId(),
            LocalDateTime.now(),
            105.0,
            "mg/dL",
            otherPatient.getId() // Different patient ID
        );

        mockMvc.perform(put("/api/patients/{patientId}/readings/{readingId}", 
                          testPatient.getId(), savedReading.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedReading)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("patientId in path does not match patientId in request body")));
    }
}
