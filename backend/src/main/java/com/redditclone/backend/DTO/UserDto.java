package com.redditclone.backend.DTO;

import com.redditclone.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private String profilePicture;
    private LocalDateTime createdAt;

    public UserDto(User user) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.profilePicture = user.getProfilePicture();
        this.createdAt = user.getCreatedAt();
    }
}
