package com.redditclone.backend.controller;

import com.redditclone.backend.DTO.comment.CommentRequest;
import com.redditclone.backend.DTO.comment.CommentResponse;
import com.redditclone.backend.service.CommentService;
import com.redditclone.backend.service.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> getAllCommentsByPostId(@PathVariable Long postId){
        return commentService.getAllCommentsByPostId(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequest commentRequest, @AuthenticationPrincipal UserPrincipal currentUser) {
        CommentResponse createdComment = commentService.createComment(postId, commentRequest, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/comments/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id, @Valid @RequestBody CommentRequest commentRequest, @AuthenticationPrincipal UserPrincipal currentUser){
        CommentResponse commentUpdated = commentService.updateComment(id, commentRequest, currentUser);
        return ResponseEntity.ok(commentUpdated);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal currentUser) {
        commentService.deleteComment(id, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comments/user/{userId}")
    public Page<CommentResponse> getCommentsByUserId(@PathVariable Long userId,
                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size){
        return commentService.getCommentsByUserId(userId, page, size);
    }


    /*

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
    } */



}
