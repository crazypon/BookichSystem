package com.ilnur.bookich.controllers;

import com.ilnur.bookich.dtos.BookRegistrationDTO;
import com.ilnur.bookich.entities.Book;
import com.ilnur.bookich.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/register")
    public ResponseEntity<String> registerBook(@Valid @RequestBody BookRegistrationDTO book) {
        bookService.register(book);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Book registered successfully");
    }

    /*
    When paginating, Pageable required. We can get it by either using @PageableDefault
    or by using .of() of PageRequest.
    @PageDefault grabs the query parameters, and if they are not given, they are replaced by default values
    (here we defined them a size=8 etc.)
     */
    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(@PageableDefault(size = 8, sort = "title") Pageable pageable) {
        Page<Book> books = bookService.getLibrary(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable long id) {
        Book book = bookService.getBook(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/my-books")
    public ResponseEntity<Page<Book>> getCurrentUsersBook(@PageableDefault(size = 8, sort = "title") Pageable pageable) {
        Page<Book> books = bookService.getUsersBook(pageable);
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable long id) {
        bookService.remove(id);
        return ResponseEntity.ok("Book Removed Successfully");
    }

}
