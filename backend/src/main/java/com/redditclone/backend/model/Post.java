package com.redditclone.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    /*@ManyToOne
    @JoinColumn(name = "subreddit_id", nullable = false)
    private Subreddit subreddit;*/

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /*@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;*/

    private Integer upvotePost = 0;
    private Integer downvotePost = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Post() {}

    public Post(User author,String title, String content) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
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

    /*public Subreddit getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }*/

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getUpvotePost() {
        return upvotePost;
    }

    public void setUpvotePost(int upvotePost) {
        this.upvotePost = upvotePost;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getDownvotePost() {
        return downvotePost;
    }

    public void setDownvotePost(int downvotePost) {
        this.downvotePost = downvotePost;
    }

    /*public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }*/
}
