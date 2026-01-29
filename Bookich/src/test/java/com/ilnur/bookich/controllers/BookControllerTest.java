package com.ilnur.bookich.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilnur.bookich.dtos.BookRegistrationDTO;
import com.ilnur.bookich.entities.Book;
import com.ilnur.bookich.enums.Condition;
import com.ilnur.bookich.enums.Genre;
import com.ilnur.bookich.services.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)

class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @WithMockUser
    @DisplayName("Register: Should return 201 Created when data is valid")
    void registerBook_Success() throws Exception {
        BookRegistrationDTO dto = new BookRegistrationDTO();
        dto.setTitle("Effective Java");
        dto.setAuthor("Joshua Bloch");
        dto.setGenre(Genre.TEXTBOOK);
        dto.setCondition(Condition.NEW);
        dto.setIs18Plus(false);
        dto.setDescription("The best book about java programming. By reading it once you will become java senior" +
                "developer");

        doNothing().when(bookService).register(any(BookRegistrationDTO.class));

        mockMvc.perform(post("/api/books/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Book registered successfully"));

        verify(bookService, times(1)).register(any(BookRegistrationDTO.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Get Library: Should return a Page of books")
    void getAllBooks_Success() throws Exception {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book A");

        Page<Book> bookPage = new PageImpl<>(List.of(book1));

        when(bookService.getLibrary(any(Pageable.class))).thenReturn(bookPage);

        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Book A"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    @DisplayName("Get Book: Should return book details")
    void getBook_Success() throws Exception {
        Book book = new Book();
        book.setId(10L);
        book.setTitle("Specific Book");

        when(bookService.getBook(10L)).thenReturn(book);

        mockMvc.perform(get("/api/books/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Specific Book"));
    }

    @Test
    @WithMockUser
    @DisplayName("Get My Books: Should return current user's library")
    void getCurrentUsersBook_Success() throws Exception {
        Book myBook = new Book();
        myBook.setTitle("My Personal Book");
        Page<Book> myBookPage = new PageImpl<>(List.of(myBook));

        when(bookService.getUsersBook(any(Pageable.class))).thenReturn(myBookPage);

        mockMvc.perform(get("/api/books/my-books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("My Personal Book"));
    }

    @Test
    @WithMockUser
    @DisplayName("Delete: Should return 200 OK")
    void deleteBook_Success() throws Exception {
        doNothing().when(bookService).remove(55L);

        mockMvc.perform(delete("/api/books/{id}", 55L)
                        .with(csrf())) // Required for DELETE
                .andExpect(status().isOk())
                .andExpect(content().string("Book Removed Successfully"));

        verify(bookService, times(1)).remove(55L);
    }
}
