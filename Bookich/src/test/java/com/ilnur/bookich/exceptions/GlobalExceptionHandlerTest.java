package com.ilnur.bookich.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // We register ONLY the ExceptionHandler and our Dummy Controller
        mockMvc = MockMvcBuilders.standaloneSetup(new DummyController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Validation Error: Should return 400 and error map")
    void handleValidationErrors_ShouldReturn400() throws Exception {
        // We send an object with a null field to trigger @NotNull
        TestDTO invalidDto = new TestDTO();

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.requiredField").value("must not be null"));
    }

    @Test
    @DisplayName("User Exists: Should return 409 and message")
    void handleUserExists_ShouldReturn409() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("User with email x already exists"));
    }

    // --- Helpers ---

    // 1. A dummy DTO to trigger validation
    @Data
    static class TestDTO {
        @NotNull(message = "must not be null")
        private String requiredField;
    }

    // 2. A dummy controller that purposely fails
    @RestController
    @RequestMapping("/test")
    static class DummyController {

        @PostMapping("/validation")
        public void testValidation(@Valid @RequestBody TestDTO dto) {
            // This method body never runs if validation fails
        }

        @GetMapping("/conflict")
        public void testConflict() {
            throw new UserAlreadyExistsException("User with email x already exists");
        }
    }
}