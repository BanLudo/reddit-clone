package com.redditclone.backend.repository;

import com.redditclone.backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    //Je recup les commentaires racines d'un post, cad, parent comment est null, et les range par ordre croissant.
    List<Comment> findByPost_PostIdAndParentIsNullOrderByCreatedAt(Long postId);

    List<Comment> findByParent_CommentIdOrderByCreatedAtAsc(Long commentParentId);

    List<Comment> findByAuthor_UserIdOrderByCreatedAtAsc(Long userId);
}
