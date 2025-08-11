package com.redditclone.backend.DTO;

import lombok.Data;

@Data
public class AuthDto {
    private String token;
    private String tokenType = "Bearer";
    private UserDto user;

    public AuthDto() {}

    public AuthDto(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }
}
