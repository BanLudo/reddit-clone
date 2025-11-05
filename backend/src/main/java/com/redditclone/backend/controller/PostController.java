package com.redditclone.backend.controller;

import com.redditclone.backend.DTO.PostRequest;
import com.redditclone.backend.DTO.PostResponse;
import com.redditclone.backend.service.PostService;
import com.redditclone.backend.service.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    public Page<PostResponse> getAllPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                                    @RequestParam(value = "sort", defaultValue = "new") String sort) {
        return postService.getAllPosts(page, size, sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest, @AuthenticationPrincipal UserPrincipal currentUser) {

        PostResponse createdPost = postService.createdPost(currentUser, postRequest);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequest postRequest, @AuthenticationPrincipal UserPrincipal currentUser) {
        PostResponse updatedPost = postService.updatePost(id, postRequest, currentUser);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePostById(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal currentUser) {
        postService.deletePost(id, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public Page<PostResponse> getPostsByUserId(@PathVariable Long userId,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size){
        return postService.getPostsByUserId(userId, page, size);
    }

    /*

    @GetMapping("/user/{username}")
    public ResponseEntity<Page<PostRequest>> getAllPostsByUsers(@PathVariable String username,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PostRequest> postsRequestByUsers = postService.getAllPostsByUsers(username, pageable);
        return ResponseEntity.ok(postsRequestByUsers);
    } */

}
