package com.ilnur.bookich.dtos;

import com.ilnur.bookich.enums.ExchangeType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExchangeResponseDTO {
    @NotNull(message = "Please, choose the exchange type")
    private ExchangeType type;

    @NotNull(message = "Please, choose the book you are swapping")
    private Long offeredBookId;

    private Long requestedBookId; // this field is optional, since we can gift
}
