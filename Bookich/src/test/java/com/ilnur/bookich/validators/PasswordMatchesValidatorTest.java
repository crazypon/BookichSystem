package com.ilnur.bookich.validators;

import com.ilnur.bookich.dtos.UserRegistrationDTO;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PasswordMatchesValidatorTest {

    private PasswordMatchesValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new PasswordMatchesValidator();
    }

    @Test
    @DisplayName("isValid: Should return true when passwords match")
    void isValid_WhenPasswordsMatch_ReturnsTrue() {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setPassword("Secret123");
        dto.setMatchPassword("Secret123");

        boolean result = validator.isValid(dto, context);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid: Should return false when passwords do not match")
    void isValid_WhenPasswordsDoNotMatch_ReturnsFalse() {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setPassword("Secret123");
        dto.setMatchPassword("WrongPass");

        boolean result = validator.isValid(dto, context);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValid: Should return false when any password is null")
    void isValid_WhenPasswordIsNull_ReturnsFalse() {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setPassword("Secret123");
        dto.setMatchPassword(null); // One is missing

        boolean result = validator.isValid(dto, context);

        assertThat(result).isFalse();
    }
}