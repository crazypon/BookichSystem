package com.ilnur.bookich.validators;

import com.ilnur.bookich.annotations.PasswordMatches;
import com.ilnur.bookich.dtos.UserRegistrationDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegistrationDTO> {
    @Override
    public boolean isValid(UserRegistrationDTO dto, ConstraintValidatorContext constraintValidatorContext) {
        if(dto.getPassword() == null || dto.getMatchPassword() == null)
            return false;

        return dto.getPassword().equals(dto.getMatchPassword());
    }

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
