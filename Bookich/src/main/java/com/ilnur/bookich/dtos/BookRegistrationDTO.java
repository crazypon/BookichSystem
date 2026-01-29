package com.ilnur.bookich.dtos;

import com.ilnur.bookich.enums.Condition;
import com.ilnur.bookich.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class BookRegistrationDTO {
    @NotBlank(message = "Please, enter the title")
    @Size(min = 5, max = 120, message = "There should be at least 5 characters")
    private String title;

    @NotBlank(message = "Please, enter the author")
    @Size(min = 5, max = 50, message = "There should be at least 5 characters")
    private String author;

    @NotNull(message = "Please, choose the genre")
    private Genre genre;

    @NotBlank(message = "Please enter the description")
    @Size(min = 40, max = 120, message = "Description should be more than 50 and less than 120 characters long")
    private String description;

    @NotNull(message = "Please, choose the current condition of the book")
    private Condition condition;

    @NotNull(message = "Please, specify if this book has explicit content")
    private Boolean is18Plus;
}
