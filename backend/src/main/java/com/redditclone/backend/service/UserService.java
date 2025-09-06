package com.redditclone.backend.service;

import com.redditclone.backend.DTO.UserProfile;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfile getCurrentUser(UserPrincipal currentUser) {
        return getUserProfile(currentUser.getId());
    }

    public UserProfile getUserProfile(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(()-> new RuntimeException("User avec l'id "+id+" not found"));

        UserProfile userProfile = new UserProfile();
        userProfile.setId(user.getId());
        userProfile.setUsername(user.getUsername());
        userProfile.setEmail(user.getEmail());
        userProfile.setProfilePicture(user.getProfilePicture());
        userProfile.setCreatedAt(user.getCreatedAt());

        return userProfile;
    }












    /*
    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new RuntimeException("User not authenticated");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(userPrincipal.getUserId())
                                  .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return new UserDto(user);
    }

    @Transactional
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

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username " + username + " not found"));

        return new UserDto(user);
    }*/
}
