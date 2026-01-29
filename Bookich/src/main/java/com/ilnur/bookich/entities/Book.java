package com.ilnur.bookich.entities;

import com.ilnur.bookich.enums.Condition;
import com.ilnur.bookich.enums.Genre;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // By default, it is EAGER
    @JoinColumn(name = "owner_id", nullable = false) // map to column in DB, in JPA we use objects, soo...
    // nullable inside of JoinColumn is same as everywhere, meaning book cannot be orphan it should have user
    @ToString.Exclude // prevent ownerId from being printed in toString()
    private User owner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(columnDefinition = "TEXT") // allows long descriptions
    private String description;

    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Column(name = "is_18_plus")
    private Boolean is18Plus = false; // false will be default value

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createAt;
}
