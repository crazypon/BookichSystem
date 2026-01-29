package com.ilnur.bookich.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotBlank(message = "Please, enter your username")
    private String username;

    @NotBlank(message = "Please, enter your password")
    private String password;
}
