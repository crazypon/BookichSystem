package com.ilnur.bookich.mappers;

import com.ilnur.bookich.dtos.ExchangeRequestDTO;
import com.ilnur.bookich.dtos.ExchangeResponseDTO;
import com.ilnur.bookich.entities.Book;
import com.ilnur.bookich.entities.ExchangeRequest;
import com.ilnur.bookich.entities.User;
import com.ilnur.bookich.repositories.BookRepository;
import com.ilnur.bookich.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;


@Mapper(componentModel = "spring")
public interface ExchangeRequestMapper {
    @Mapping(source="offeredBook.id", target = "offeredBookId")
    @Mapping(source="requestedBook.id", target = "requestedBookId")
    public ExchangeResponseDTO toResponseDTO(ExchangeRequest request);

    /*
    Mapping id and object
     */

    @Mapping(target = "offeredBook", ignore = true)
    @Mapping(target = "requestedBook",ignore = true)
    @Mapping(target = "receiver", ignore = true)
    public ExchangeRequest toExchangeRequest(ExchangeRequestDTO dto);



}
