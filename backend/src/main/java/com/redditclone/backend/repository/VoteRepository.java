package com.redditclone.backend.repository;

import com.redditclone.backend.model.Comment;
import com.redditclone.backend.model.Post;
import com.redditclone.backend.model.User;
import com.redditclone.backend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserAndPost(User user, Post post);

    Optional<Vote> findByUserAndComment(User user, Comment comment);

    @Query("SELECT COALESCE(SUM(CASE v.voteType " +
            "WHEN com.redditclone.backend.enumeration.VoteType.UPVOTE THEN 1 " +
            "WHEN com.redditclone.backend.enumeration.VoteType.DOWNVOTE THEN 0 - 1 " +
            "END), 0) " +
            "FROM Vote v WHERE v.comment = :comment")
    int getCommentScore(@Param("comment") Comment comment);

    @Query("SELECT COALESCE(SUM(CASE v.voteType " +
            "WHEN com.redditclone.backend.enumeration.VoteType.UPVOTE THEN 1 " +
            "WHEN com.redditclone.backend.enumeration.VoteType.DOWNVOTE THEN 0 - 1 " +
            "END), 0) " +
            "FROM Vote v WHERE v.post = :post")
    int getPostScore(@Param("post") Post post);

}
