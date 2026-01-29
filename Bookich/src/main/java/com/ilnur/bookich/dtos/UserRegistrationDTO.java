package com.ilnur.bookich.dtos;

import com.ilnur.bookich.annotations.PasswordMatches;
import com.ilnur.bookich.enums.District;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches // custom validation, checks if password and matchPassword are equal
public class UserRegistrationDTO {
    @NotBlank(message = "please enter your username")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    // Regex: At least one letter and one number
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
            message = "Password must contain letters and numbers")
    private String password;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    // Regex: At least one letter and one number
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
            message = "Password must contain letters and numbers")
    @Transient // this annotation tells JPA, not to consider this field when it queries the database
    private String matchPassword;

    @NotBlank(message = "Please, enter your first name")
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank(message = "Please, enter your lastName")
    @Size(min = 3, max = 20)
    private String lastName;

    @NotBlank(message = "Please, enter your phone number")
    @Pattern(regexp = "^\\+998[0-9]{9}$")
    private String phoneNumber;

    // NotBlank is validator exclusively for String objects NotNull is for any object
    @NotNull(message = "Please, enter you district")
    private District district;
}
