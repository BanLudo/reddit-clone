package com.redditclone.backend.DTO.comment;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {

    @NotBlank
    private String content;

    private Long parentId;

    public @NotBlank String getAuthor() {
        return content;
    }

    public void setAuthor(@NotBlank String author) {
        this.content = author;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public @NotBlank String getContent() {
        return content;
    }

    public void setContent(@NotBlank String content) {
        this.content = content;
    }
}
