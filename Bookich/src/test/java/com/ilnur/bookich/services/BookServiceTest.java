package com.ilnur.bookich.services;

import com.ilnur.bookich.dtos.BookRegistrationDTO;
import com.ilnur.bookich.entities.Book;
import com.ilnur.bookich.entities.User;
import com.ilnur.bookich.mappers.BookMapper;
import com.ilnur.bookich.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserContextService userContextService;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private User mockUser;
    private Book mockBook;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUsername("testUser");

        mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setOwner(mockUser);
        mockBook.setTitle("Test Title");
    }

    @Test
    void register_ShouldSaveBookWithCurrentUserAsOwner() {
        BookRegistrationDTO dto = new BookRegistrationDTO();
        when(userContextService.getCurrentUser()).thenReturn(mockUser);
        when(bookMapper.toBook(dto)).thenReturn(new Book());

        bookService.register(dto);

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getBook_WhenIdExists_ShouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        Book found = bookService.getBook(1L);

        assertEquals("Test Title", found.getTitle());
    }

    @Test
    void getBook_WhenIdDoesNotExist_ShouldThrowNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookService.getBook(1L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void remove_WhenUserIsOwner_ShouldDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(userContextService.getCurrentUser()).thenReturn(mockUser);

        bookService.remove(1L);

        verify(bookRepository, times(1)).delete(mockBook);
    }

    @Test
    void remove_WhenUserIsNotOwner_ShouldThrowForbidden() {
        User differentUser = new User();
        differentUser.setUsername("hacker");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook)); // Owner is testUser
        when(userContextService.getCurrentUser()).thenReturn(differentUser);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookService.remove(1L));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        verify(bookRepository, never()).delete(any());
    }
}