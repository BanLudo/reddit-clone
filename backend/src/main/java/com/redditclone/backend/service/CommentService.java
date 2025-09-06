package com.redditclone.backend.service;

import com.redditclone.backend.DTO.comment.CommentRequest;
import com.redditclone.backend.DTO.comment.CommentResponse;
import com.redditclone.backend.model.Comment;
import com.redditclone.backend.model.Post;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.CommentRepository;
import com.redditclone.backend.repository.PostRepository;
import com.redditclone.backend.repository.UserRepository;
import com.redditclone.backend.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, VoteRepository voteRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.voteRepository = voteRepository;
    }

    public List<CommentResponse> getAllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPost_IdAndParentIsNullOrderByVoteCountDesc(postId);

        return comments.stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }


    public CommentResponse createComment(Long postId, @Valid CommentRequest commentRequest, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post Not Found"));

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setPost(post);
        comment.setUser(user);

        if(commentRequest.getParentId() != null) {
            Comment parent = commentRepository.findById(commentRequest.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent Comment Not Found"));
            comment.setParent(parent);
        }

        Comment savedComment = commentRepository.save(comment);

        return mapToCommentResponse(savedComment);
    }

    @Transactional
    public CommentResponse updateComment(Long id, @Valid CommentRequest commentRequest, UserPrincipal currentUser) {
        Comment comment = commentRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException("Comment Not Found"));

        if(!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No permission to edit this comment");
        }

        comment.setContent(commentRequest.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return mapToCommentResponse(updatedComment);
    }


    public void deleteComment(Long id, UserPrincipal currentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("comment with Id " + id + " not Found"));

        if(!comment.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("Can only delete your own comment");
        }
        commentRepository.delete(comment);
    }



    private CommentResponse mapToCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setId(comment.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setUsername(comment.getUser().getUsername());
        commentResponse.setUserId(comment.getUser().getId());
        commentResponse.setVoteCount(comment.getVoteCount());
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setUpdatedAt(comment.getUpdatedAt());

        if(comment.getParent() != null) {
            commentResponse.setParentId(comment.getParent().getId());
        }

        List<CommentResponse> replies = comment.getReplies().stream()
                                                            .map(this::mapToCommentResponse)
                                                            .collect(Collectors.toList());
        commentResponse.setReplies(replies);
        return commentResponse;
    }


    /*

    public List<CommentResponseDTO> getAllCommentsByUser(Long userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new RuntimeException("User with Id " + userId + " Not Found"));

        List<Comment> comments = commentRepository.findByAuthor_UserIdOrderByCreatedAtAsc(user.getUserId());

        return comments.stream()
                .map(com -> new CommentResponseDTO(com))
                .toList();
    }


    public List<CommentResponseDTO> getAllRepliesByComment(Long commentId) {
        List<Comment> replies = commentRepository.findByParent_CommentIdOrderByCreatedAtAsc(commentId);

        if(replies.isEmpty()){
            throw new EntityNotFoundException("Comment with Id " + commentId + " Not Found");
        }
        return replies.stream()
                      .map((com) -> new CommentResponseDTO(com))
                      .toList();
    }  */

}
