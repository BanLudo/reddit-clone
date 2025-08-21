package com.redditclone.backend.DTO;

import com.redditclone.backend.model.Post;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class PostRequest {
    private long postId;

    @NotBlank(message = "Title is required")
    private String title;

    private String content;
    private String imageUrl;
    private String author;
    private LocalDateTime createdAt;
    private int upvotes;
    private Integer downvotes;
    private Integer commentCount;

    public PostRequest() {}

    public PostRequest(Post post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.author = post.getAuthor().getUsername();
        this.createdAt = post.getCreatedAt();
        this.upvotes = post.getUpvotePost();
        this.downvotes = post.getDownvotePost();
        /*this.commentCount = post.getComments() != null ?  0 : post.getComments().size();*/
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public @NotBlank(message = "Title is required") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") String title) {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public Integer getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(Integer downvotes) {
        this.downvotes = downvotes;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}
