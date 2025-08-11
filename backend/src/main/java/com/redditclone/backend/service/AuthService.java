package com.redditclone.backend.service;

import com.redditclone.backend.DTO.AuthDto;
import com.redditclone.backend.DTO.RegisterRequest;
import com.redditclone.backend.DTO.UserDto;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.UserRepository;
import com.redditclone.backend.service.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public AuthDto register(RegisterRequest registerRequest) {

        Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());

        if (userOptional.isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword( passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);

        String jwt = jwtTokenProvider.generateToken(savedUser.getEmail());

        return new AuthDto(jwt, new UserDto(savedUser));
    }
}
