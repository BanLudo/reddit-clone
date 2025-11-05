package com.redditclone.backend.service;

import com.redditclone.backend.DTO.UserProfile;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.CommentRepository;
import com.redditclone.backend.repository.PostRepository;
import com.redditclone.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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

        //stats utilisateur
        userProfile.setPostCount(postRepository.countByUserId(id));
        userProfile.setCommentCount((long) commentRepository.findByUser_IdOrderByCreatedAtDesc(id).size());

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
