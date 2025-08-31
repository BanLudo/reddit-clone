package com.redditclone.backend.service;

import com.redditclone.backend.DTO.vote.VoteDto;
import com.redditclone.backend.enumeration.VoteType;
import com.redditclone.backend.model.Comment;
import com.redditclone.backend.model.Post;
import com.redditclone.backend.model.User;
import com.redditclone.backend.model.Vote;
import com.redditclone.backend.repository.CommentRepository;
import com.redditclone.backend.repository.PostRepository;
import com.redditclone.backend.repository.UserRepository;
import com.redditclone.backend.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoteService {


    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public VoteService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }


    @Transactional
    public void vote(VoteDto voteDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean hasPostId = voteDto.getPostId() != null;
        boolean hasCommentId = voteDto.getCommentId() != null;

        if(hasPostId == hasCommentId) {
            throw new RuntimeException("You must vote on either a post OR a comment (not both" );
        }

        if(hasPostId) {
            Post post = postRepository.findById(voteDto.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            processVote(user,voteDto.getVoteType(), post, null);
        }else {
            Comment comment = commentRepository.findById(voteDto.getCommentId())
                    .orElseThrow(()-> new RuntimeException("Comment not found"));

            processVote(user, voteDto.getVoteType(), null, comment);
        }

        //mise à jour du score
        updateScore(voteDto);
    }

    public int getScore(VoteDto voteDto) {
        if(voteDto.getPostId() != null) {
            return postRepository.findById(voteDto.getPostId())
                                 .orElseThrow(()-> new RuntimeException("Post not found"))
                                 .getVoteCount();
        }else {
            return commentRepository.findById(voteDto.getCommentId())
                                    .orElseThrow(()-> new RuntimeException("Comment not found"))
                                    .getVoteCount();
        }
    }


    private void processVote(User user, VoteType voteType, Post post, Comment comment) {

        Optional<Vote> existingVoteOpt;

        if (post != null) {
            existingVoteOpt = voteRepository.findByUserAndPost(user, post);
        } else {
            existingVoteOpt = voteRepository.findByUserAndComment(user, comment);
        }

        if (existingVoteOpt.isPresent()) {
            Vote existingVote = existingVoteOpt.get();
            if (existingVote.getVoteType() == voteType) {
                voteRepository.delete(existingVote); // toggle off
            } else {
                existingVote.setVoteType(voteType); // change vote
                voteRepository.save(existingVote);
            }
        } else {
            // nouveau vote
            Vote newVote = new Vote();
            newVote.setUser(user);
            newVote.setVoteType(voteType);
            newVote.setCreatedAt(LocalDateTime.now());
            if (post != null) newVote.setPost(post);
            if (comment != null) newVote.setComment(comment);
            voteRepository.save(newVote);
        }
    }

    // recalcule le score depuis table vote
    private void updateScore(VoteDto voteDto) {

        if (voteDto.getPostId() != null) {
            Post post = postRepository.findById(voteDto.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            post.setVoteCount(voteRepository.getPostScore(post));
            postRepository.save(post);
        } else {
            Comment comment = commentRepository.findById(voteDto.getCommentId())
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            comment.setVoteCount(voteRepository.getCommentScore(comment));
            commentRepository.save(comment);
        }
    }

} // fin de service
