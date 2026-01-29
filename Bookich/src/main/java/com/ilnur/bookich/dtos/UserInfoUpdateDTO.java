package com.ilnur.bookich.dtos;

import com.ilnur.bookich.enums.District;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserInfoUpdateDTO {

    private String username;

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^\\+998[0-9]{9}$")
    private String phoneNumber;

    private District district;
}
