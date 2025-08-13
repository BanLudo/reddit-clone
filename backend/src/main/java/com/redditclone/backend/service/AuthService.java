package com.redditclone.backend.service;

import com.redditclone.backend.DTO.AuthDto;
import com.redditclone.backend.DTO.LoginRequest;
import com.redditclone.backend.DTO.RegisterRequest;
import com.redditclone.backend.DTO.UserDto;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.UserRepository;
import com.redditclone.backend.service.jwt.JwtTokenProvider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
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

    public AuthDto login(LoginRequest loginRequest) {
        Authentication loginAttempt = new UsernamePasswordAuthenticationToken(
                                                    loginRequest.getEmail(),
                                                    loginRequest.getPassword());

        Authentication authResult = authenticationManager.authenticate(loginAttempt);

        SecurityContextHolder.getContext().setAuthentication(authResult);
        UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(userPrincipal.getEmail());

        Optional<User> optionalUser = userRepository.findByEmail(userPrincipal.getEmail());
        if(optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }

        return new AuthDto(jwt,new UserDto(optionalUser.get()));
    }
}
