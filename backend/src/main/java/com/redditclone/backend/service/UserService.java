package com.redditclone.backend.service;

import com.redditclone.backend.DTO.UserDto;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getCurrentUser(String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();
        return new UserDto(user);
    }

    public UserDto updateProfile(String userEmail, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();
        user.setUsername(userDto.getUsername());
        userRepository.save(user);

        return new UserDto(user);
    }


    public UserDto getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with Id of " + userId);
        }
        User user = optionalUser.get();
        return new UserDto(user);
    }

}
