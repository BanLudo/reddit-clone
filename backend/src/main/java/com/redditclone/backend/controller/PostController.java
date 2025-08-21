package com.redditclone.backend.controller;

import com.redditclone.backend.DTO.PostRequest;
import com.redditclone.backend.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostRequest> createPost(@Valid @RequestBody PostRequest postRequest, Authentication authentication) {
        String userEmail = authentication.getName();
        PostRequest createdPost = postService.createdPost(userEmail, postRequest);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostRequest> getPostById(@PathVariable Long id) {
        PostRequest postRequest = postService.getPostById(id);
        return ResponseEntity.ok(postRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        postService.deletePost(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PostRequest>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10")int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostRequest> postRequests = postService.getAllPosts(pageable);
        return ResponseEntity.ok(postRequests);
    }


}
