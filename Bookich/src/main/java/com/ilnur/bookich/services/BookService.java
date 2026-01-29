package com.ilnur.bookich.services;

import com.ilnur.bookich.dtos.BookRegistrationDTO;
import com.ilnur.bookich.entities.Book;
import com.ilnur.bookich.entities.User;
import com.ilnur.bookich.mappers.BookMapper;
import com.ilnur.bookich.repositories.BookRepository;
import com.ilnur.bookich.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;



@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final BookMapper bookMapper;

    public void register(BookRegistrationDTO dto) {
        User owner = userContextService.getCurrentUser();

        Book book = bookMapper.toBook(dto);
        book.setOwner(owner);
        bookRepository.save(book);
    }

    public Page<Book> getLibrary(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book getBook(Long id) {
        // ResponseEntityException will automatically return a response with 404 in this case
        // if it gets raised
        return bookRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Such book")
        );
    }

    public Page<Book> getUsersBook(Pageable pageable) {
        String user = userContextService.getCurrentUser().getUsername();
        // Please, pay attention to the name of this method OwnerUsername, that saves a lot of time, since
        // entity contains only User owner, not String owner
        return bookRepository.findBooksByOwnerUsername(user, pageable);
    }

    public void remove(long id) {
        // check if book exists
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Such Book")
        );
        String currentUser = userContextService.getCurrentUser().getUsername();

        // check if client has right to remove this book
        if(!book.getOwner().getUsername().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You Cannot Delete This Book");
        }

        bookRepository.delete(book);
    }

}
