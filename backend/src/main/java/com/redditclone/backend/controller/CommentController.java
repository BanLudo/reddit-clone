package com.redditclone.backend.controller;

import com.redditclone.backend.DTO.comment.CommentResponseDTO;
import com.redditclone.backend.DTO.comment.CreateCommentDTO;
import com.redditclone.backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

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

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByPost(@PathVariable Long postId){
        List<CommentResponseDTO> comments = commentService.getAllCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        commentService.delete(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByUser(@PathVariable Long userId) {
        List<CommentResponseDTO> comments = commentService.getAllCommentsByUser(userId);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentResponseDTO>> getAllRepliesByComment(@PathVariable Long commentId){
        List<CommentResponseDTO> replies = commentService.getAllRepliesByComment(commentId);
        return ResponseEntity.ok(replies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long id, @Valid @RequestBody CreateCommentDTO updateRequest, Authentication authentication) throws AccessDeniedException {
        String userEmail = authentication.getName();
        CommentResponseDTO commentUpdated = commentService.updateComment(id, userEmail, updateRequest);
        return ResponseEntity.ok(commentUpdated);
    }



}
