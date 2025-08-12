package com.redditclone.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @Email(message = "Provide a correct email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "username between 3 and 20 words")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be min 6 characters")
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
