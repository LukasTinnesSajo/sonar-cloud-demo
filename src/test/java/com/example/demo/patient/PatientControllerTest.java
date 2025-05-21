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
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.example.demo.DemoApplication.class)
@AutoConfigureMockMvc
@Transactional // Rollback transactions after each test
@ActiveProfiles("test") // Ensure test properties are loaded
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For LocalDate serialization
        patientRepository.deleteAll(); // Clean up before each test
    }

    @Test
    void createPatient_shouldReturnCreatedPatient() throws Exception {
        PatientDTO patientDTO = new PatientDTO(null, "John", "Doe", LocalDate.of(1990, 1, 1), new ArrayList<>());

        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    void getAllPatients_shouldReturnListOfPatients() throws Exception {
        Patient patient1 = patientRepository.save(new Patient("Jane", "Doe", LocalDate.of(1985, 5, 5)));
        Patient patient2 = patientRepository.save(new Patient("Jim", "Beam", LocalDate.of(1970, 3, 15)));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is(patient1.getFirstName())))
                .andExpect(jsonPath("$[1].firstName", is(patient2.getFirstName())));
    }

    @Test
    void getPatientById_whenPatientExists_shouldReturnPatient() throws Exception {
        Patient patient = patientRepository.save(new Patient("Alice", "Smith", LocalDate.of(2000, 10, 10)));

        mockMvc.perform(get("/api/patients/" + patient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(patient.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("Alice")));
    }

    @Test
    void getPatientById_whenPatientDoesNotExist_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePatient_whenPatientExists_shouldReturnUpdatedPatient() throws Exception {
        Patient existingPatient = patientRepository.save(new Patient("Bob", "Marley", LocalDate.of(1945, 2, 6)));
        PatientDTO updatedDetails = new PatientDTO(existingPatient.getId(), "Robert", "Marley", LocalDate.of(1945, 2, 6), new ArrayList<>());

        mockMvc.perform(put("/api/patients/" + existingPatient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Robert")));
    }

    @Test
    void updatePatient_whenPatientDoesNotExist_shouldReturnNotFound() throws Exception {
        PatientDTO updatedDetails = new PatientDTO(999L, "Non", "Existent", LocalDate.now(), new ArrayList<>());
        mockMvc.perform(put("/api/patients/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePatient_whenPatientExists_shouldReturnNoContent() throws Exception {
        Patient patient = patientRepository.save(new Patient("Delete", "Me", LocalDate.now()));
        mockMvc.perform(delete("/api/patients/" + patient.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/patients/" + patient.getId()))
                .andExpect(status().isNotFound()); // Verify patient is actually deleted
    }

    @Test
    void deletePatient_whenPatientDoesNotExist_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/patients/999"))
                .andExpect(status().isNotFound());
    }

    // Input validation tests
    @Test
    void createPatient_withEmptyFirstName_shouldReturnBadRequest() throws Exception {
        PatientDTO patientDTO = new PatientDTO(null, "", "Doe", LocalDate.of(1990, 1, 1), new ArrayList<>());
        
        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatient_withNullLastName_shouldReturnBadRequest() throws Exception {
        PatientDTO patientDTO = new PatientDTO(null, "John", null, LocalDate.of(1990, 1, 1), new ArrayList<>());
        
        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatient_withFutureDateOfBirth_shouldReturnBadRequest() throws Exception {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        PatientDTO patientDTO = new PatientDTO(null, "John", "Doe", futureDate, new ArrayList<>());
        
        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePatient_withInvalidIdMismatch_shouldReturnBadRequest() throws Exception {
        Patient patient = patientRepository.save(new Patient("Original", "Patient", LocalDate.of(1990, 1, 1)));
        PatientDTO updatedDetails = new PatientDTO(patient.getId() + 1, "Updated", "Patient", LocalDate.of(1990, 1, 1), new ArrayList<>());
        
        mockMvc.perform(put("/api/patients/" + patient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("ID in the path does not match the ID in the request body")));
    }
}
