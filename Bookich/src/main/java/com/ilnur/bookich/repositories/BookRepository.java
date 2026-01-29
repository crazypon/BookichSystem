package com.ilnur.bookich.repositories;

import com.ilnur.bookich.entities.Book;
import com.ilnur.bookich.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findBooksByOwnerUsername(String owner, Pageable pageable);
    Page<Book> findAll(Pageable pageable);
}
