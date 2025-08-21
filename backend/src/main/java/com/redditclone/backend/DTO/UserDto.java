package com.redditclone.backend.DTO;

import com.redditclone.backend.model.User;

import java.time.LocalDateTime;


public class UserDto {
    private Long id;
    private String email;
    private String username;
    private String profilePicture;
    private LocalDateTime createdAt;

    public UserDto() {}

    public UserDto(User user) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.profilePicture = user.getProfilePicture();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
