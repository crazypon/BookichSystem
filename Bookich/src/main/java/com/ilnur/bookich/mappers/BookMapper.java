package com.ilnur.bookich.mappers;

import com.ilnur.bookich.dtos.BookRegistrationDTO;
import com.ilnur.bookich.entities.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toBook(BookRegistrationDTO bookRegistrationDTO);
}
