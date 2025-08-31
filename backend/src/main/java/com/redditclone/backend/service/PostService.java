package com.redditclone.backend.service;

import com.redditclone.backend.DTO.PostRequest;
import com.redditclone.backend.model.Post;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.PostRepository;
import com.redditclone.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public PostRequest createdPost(String userEmail, @Valid PostRequest postRequest) {
        Optional<User> OptionalUser  = userRepository.findByEmail(userEmail);

        if (OptionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setImageUrl(postRequest.getImageUrl());
        post.setAuthor(OptionalUser.get());
        post.setVoteCount(postRequest.getVoteCount());

        Post savedPost = postRepository.save(post);

        return new PostRequest(savedPost);
    }

    public void deletePost(Long id, String userEmail) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        if(!post.get().getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("Can only delete your own post");
        }

        postRepository.delete(post.get());
    }


    public PostRequest getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        return new PostRequest(post.get());
    }

    public Page<PostRequest> getAllPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
                             .map(post -> new PostRequest(post));
    }


    public Page<PostRequest> getAllPostsByUsers(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return postRepository.findByAuthor(user, pageable)
                             .map(post -> new PostRequest(post));
    }
}
