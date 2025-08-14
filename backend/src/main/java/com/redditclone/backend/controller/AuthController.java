package com.redditclone.backend.controller;

import com.redditclone.backend.DTO.AuthDto;
import com.redditclone.backend.DTO.LoginRequest;
import com.redditclone.backend.DTO.RegisterRequest;
import com.redditclone.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDto> register(@Valid @RequestBody RegisterRequest registerRequest){
        AuthDto response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@Valid @RequestBody LoginRequest loginRequest){
        AuthDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
