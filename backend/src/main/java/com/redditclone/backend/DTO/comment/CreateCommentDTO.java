package com.redditclone.backend.DTO.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCommentDTO {
    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotBlank(message = "Content can not be empty")
    private String content;

    private Long parentId;

    public CreateCommentDTO() {
    }

    public CreateCommentDTO(Long postId, String content, Long parentId) {
        this.postId = postId;
        this.content = content;
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public @NotBlank(message = "Content can not be empty") String getContent() {
        return content;
    }

    public void setContent(@NotBlank(message = "Content can not be empty") String content) {
        this.content = content;
    }

    public @NotNull(message = "Post ID is required") Long getPostId() {
        return postId;
    }

    public void setPostId(@NotNull(message = "Post ID is required") Long postId) {
        this.postId = postId;
    }
}
