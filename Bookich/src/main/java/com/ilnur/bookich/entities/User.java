package com.ilnur.bookich.entities;

import com.ilnur.bookich.enums.District;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


/*
CascadeTypes - they say what shall we do with child objects (foreign objects) when we perform some operation on the
parent. If there is no cascade type, then no action is performed on the child class or classes, even if we pass
the data.

CascadeType.MERGE -- Updates the child object when parent object is modified (we give the data for child as well)
CascadeType.REFRESH -- Refreshes the child object when parent object is refreshed. refresh() get's the latest updates
from the database.
CascadeType.REMOVE -- associated child rows are deleted when we remove the parent row
CascadeType.DETACH -- detach() it just stops tracking the object
CascadeType.ALL -- for all those above

Or you can manage which ones you want by giving an array of cascade types
 */

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id")
    private String publicId;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "avatar_url")
    private String avatarURL;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING) // By default, it is INTEGER, that's why we should explicitly set as STRING
    private District district;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // generating public id
    @PrePersist
    public void generatePublicId() {
        if (this.publicId == null) {
            // Generates a random number between 100000 and 999999
            int randomNum = 100000 + (int)(Math.random() * 900000);
            this.publicId = String.valueOf(randomNum);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // getUsername() and getPassword() are already handled by Lombok

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
