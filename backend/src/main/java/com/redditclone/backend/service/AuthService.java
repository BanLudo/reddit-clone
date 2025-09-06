package com.redditclone.backend.service;

import com.redditclone.backend.DTO.ApiResponse;
import com.redditclone.backend.DTO.AuthResponse;
import com.redditclone.backend.DTO.LoginRequest;
import com.redditclone.backend.DTO.SignUpRequest;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.UserRepository;
import com.redditclone.backend.security.JwtTokenProvider;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }


    public ApiResponse register(SignUpRequest signUpRequest) throws BadRequestException {

        Optional<User> userOptionalEmail = userRepository.findByEmail(signUpRequest.getEmail());
        Optional<User> userOptionalUsername = userRepository.findByUsername(signUpRequest.getUsername());

        if (userOptionalEmail.isPresent()) {
            throw new BadRequestException("Email "+signUpRequest.getEmail()+" already used.. find another one");
        }

        if (userOptionalUsername.isPresent()) {
            throw new BadRequestException("Username "+signUpRequest.getUsername()+" already used.. find another one");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword( passwordEncoder.encode(signUpRequest.getPassword()));

        User savedUser = userRepository.save(user);

        return new ApiResponse(true, "User registered successfully");
    }



    public AuthResponse login(LoginRequest loginRequest) {

        Authentication loginAttempt = new UsernamePasswordAuthenticationToken(
                                                    loginRequest.getEmail(),
                                                    loginRequest.getPassword());

        Authentication authResult = authenticationManager.authenticate(loginAttempt);

        SecurityContextHolder.getContext().setAuthentication(authResult);

        String token = jwtTokenProvider.generateToken(authResult);

        return new AuthResponse(token);

    }



}
