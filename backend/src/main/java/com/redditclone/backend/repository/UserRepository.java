package com.redditclone.backend.repository;

import com.redditclone.backend.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Email(message = "Provide a correct email")
    @NotBlank(message = "Email is required")
    Optional<User> findByEmail(String email);

}
