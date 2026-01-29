package com.ilnur.bookich.annotations;

import com.ilnur.bookich.validators.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PasswordMatches {
    String message() default "passwords are not matching";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
