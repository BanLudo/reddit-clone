package com.redditclone.backend.repository;

import com.redditclone.backend.enumeration.VoteType;
import com.redditclone.backend.model.Comment;
import com.redditclone.backend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {


    Optional<Vote> findByPost_IdAndUser_Id(Long postId, Long id);

    Optional<Vote> findByComment_IdAndUser_Id(Long commentId, Long id);

    @Query("select count(v) from Vote v where v.post.id = :postId and v.voteType = :voteType")
    Long countByPostIdAndVoteType(Long postId, VoteType voteType);

    @Query("select count(v) from Vote v where v.comment.id = :postId and v.voteType = :voteType")
    Long countByCommentIdAndVoteType(Long postId, VoteType voteType);

}
