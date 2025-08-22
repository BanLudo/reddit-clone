package com.redditclone.backend.controller;

import com.redditclone.backend.DTO.comment.CommentResponseDTO;
import com.redditclone.backend.DTO.comment.CreateCommentDTO;
import com.redditclone.backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CreateCommentDTO request, Authentication authentication) {
        String userEmail = authentication.getName();
        CommentResponseDTO createdComment = commentService.createComment(userEmail, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);

    }

    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        commentService.delete(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentRequest> updateComment(@Valid @RequestBody CommentRequest commentRequest, Authentication authentication) {
        String userEmail = authentication.getName();
        CommentRequest commentUpdated = commentService.updateComment(userEmail, commentRequest);
        return new ResponseEntity<>(commentUpdated, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentRequest>> getAllCommentsByUser(@PathVariable Long userId) {
        List<CommentRequest> comments = commentService.getAllCommentsByUser(userId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentRequest>> getAllCommentsByPost(@PathVariable Long postId){
        List<CommentRequest> comments = commentService.getAllCommentsByPost(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentRequest>> getAllRepliesByComment(@PathVariable Long commentId){
        List<CommentRequest> replies = commentService.getAllRepliesByComment(commentId);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    } */

}
