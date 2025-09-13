package com.redditclone.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 300)
    private String title;

    private String content;
    private String imageUrl;


    public @NotBlank(message = "Title is required") @Size(max = 300) String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") @Size(max = 300) String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
