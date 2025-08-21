package com.redditclone.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subreddit")
public class Subreddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subredditId;

    @Column(nullable = false)
    @NotBlank(message = "Subreddit name is required")
    private String subredditName;

    @Column(nullable = false)
    @NotBlank(message = "Description is required")
    private String description;

    /*@OneToMany(mappedBy = "subreddit",cascade = CascadeType.ALL)
    private List<Post> posts;*/

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
