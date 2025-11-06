package com.redditclone.backend.repository;

import com.redditclone.backend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.post.id = :postId and c.parent is null  order by c.voteCount desc ")
    List<Comment> findByPost_IdAndParentIsNullOrderByVoteCountDesc(Long postId);

    @Query("select c from Comment c where c.parent.id = :parentId order by c.voteCount desc, c.createdAt asc ")
    List<Comment> findByParent_idOrderByVoteCountDesc(Long parentId);

    @Query("select c from Comment c where c.user.id = :userId order by c.createdAt desc ")
    List<Comment> findByUser_IdOrderByCreatedAtDesc(Long userId);

    Page<Comment> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Long countByUser_Id(Long userId);
}
