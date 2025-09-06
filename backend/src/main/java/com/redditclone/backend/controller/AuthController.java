package com.redditclone.backend.controller;

import com.redditclone.backend.DTO.ApiResponse;
import com.redditclone.backend.DTO.AuthResponse;
import com.redditclone.backend.DTO.LoginRequest;
import com.redditclone.backend.DTO.SignUpRequest;
import com.redditclone.backend.service.AuthService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
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

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpRequest signUpRequest) throws BadRequestException {
        ApiResponse response = authService.register(signUpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
