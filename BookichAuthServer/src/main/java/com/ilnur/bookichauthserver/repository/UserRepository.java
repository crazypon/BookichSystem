package com.ilnur.bookichauthserver.repository;

import com.ilnur.bookichauthserver.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);
}
