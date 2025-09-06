package com.redditclone.backend.controller;

import com.redditclone.backend.DTO.UserProfile;
import com.redditclone.backend.service.UserPrincipal;
import com.redditclone.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserProfile getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        return userService.getCurrentUser(currentUser);
    }


    @GetMapping("/{userId}")
    public UserProfile getUserProfile(@PathVariable Long userId) {
        return userService.getUserProfile(userId);
    }

}
