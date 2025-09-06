package com.redditclone.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints={
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 50)
    private String username;

    @Column(unique = true, nullable = false)
    @Email
    @Size(max = 50)
    private String email;

    private String password;

    private Boolean emailVerified = false;

    //@Column(nullable = false) normalement je vais le rendre obligatoire !! mais pour test postman, on le laisse.
    private String profilePicture;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Vote> votes = new HashSet<>();

    public User() {}

    /*public User(String email, String username){
        this.email = email;
        this.username = username;
    }*/

    public User(Long id, String username, String email, String password, Boolean emailVerified, LocalDateTime createdAt, String profilePicture, LocalDateTime updatedAt, Set<Post> posts, Set<Comment> comments, Set<Vote> votes) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
        this.profilePicture = profilePicture;
        this.updatedAt = updatedAt;
        this.posts = posts;
        this.comments = comments;
        this.votes = votes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public @Email @Size(max = 50) String getEmail() {
        return email;
    }

    public void setEmail(@Email @Size(max = 50) String email) {
        this.email = email;
    }

    public @Size(max = 50) String getUsername() {
        return username;
    }

    public void setUsername(@Size(max = 50) String username) {
        this.username = username;
    }
}
