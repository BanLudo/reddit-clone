package com.redditclone.backend.DTO.comment;

import com.redditclone.backend.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentResponseDTO {
    private Long id;
    private String content;
    private String authorUsername;
    private Long postId;
    private Long authorId;
    private Long parentId;
    private LocalDateTime createdAt;
    private List<CommentResponseDTO> replies = new ArrayList<>();

    public CommentResponseDTO(){}

    public CommentResponseDTO(Comment comment) {
        this.id = comment.getCommentId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getPostId();
        this.authorUsername = comment.getAuthor().getUsername();
        this.authorId = comment.getAuthor().getUserId();
        this.parentId = comment.getParent() != null ? comment.getParent().getCommentId() : null;
        this.createdAt = comment.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentResponseDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentResponseDTO> replies) {
        this.replies = replies;
    }
    
}
