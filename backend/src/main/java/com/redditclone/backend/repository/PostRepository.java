package com.redditclone.backend.repository;

import com.redditclone.backend.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p order by p.createdAt desc")
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("select p from Post p order by p.voteCount desc, p.createdAt desc")
    Page<Post> findAllOrderByVoteCountDesc(Pageable pageable);

    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
