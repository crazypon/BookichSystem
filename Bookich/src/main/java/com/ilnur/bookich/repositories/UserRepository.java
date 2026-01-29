package com.ilnur.bookich.repositories;

import com.ilnur.bookich.entities.Book;
import com.ilnur.bookich.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Page<Book> findBooksByUsername(String username, Pageable pageable);

    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String username);

    Optional<User> findByPublicId(String publicId);
}
