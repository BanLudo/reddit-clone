package com.redditclone.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class SignUpRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "username between 3 and 20 words")
    private String username;

    @Email(message = "Provide a correct email")
    @NotBlank(message = "Email is required")
    @Size(max = 50)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 20, message = "Password must be min 6 characters")
    private String password;

    public @NotBlank(message = "Username is required") @Size(min = 3, max = 50, message = "username between 3 and 20 words") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required") @Size(min = 3, max = 50, message = "username between 3 and 20 words") String username) {
        this.username = username;
    }

    public @Email(message = "Provide a correct email") @NotBlank(message = "Email is required") @Size(max = 50) String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Provide a correct email") @NotBlank(message = "Email is required") @Size(max = 50) String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password is required") @Size(min = 3, max = 20, message = "Password must be min 6 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 3, max = 20, message = "Password must be min 6 characters") String password) {
        this.password = password;
    }
}
