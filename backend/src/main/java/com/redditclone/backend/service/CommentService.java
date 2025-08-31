package com.redditclone.backend.service;

import com.redditclone.backend.DTO.comment.CommentResponseDTO;
import com.redditclone.backend.DTO.comment.CreateCommentDTO;
import com.redditclone.backend.model.Comment;
import com.redditclone.backend.model.Post;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.CommentRepository;
import com.redditclone.backend.repository.PostRepository;
import com.redditclone.backend.repository.UserRepository;
import com.redditclone.backend.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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


    public CommentResponseDTO createComment(String userEmail, CreateCommentDTO commentRequest) {
        User user = userRepository.findByEmail(userEmail)
                                  .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(commentRequest.getPostId())
                                  .orElseThrow(() -> new RuntimeException("Post Not Found"));

        Comment parentComment = null;

        Long parentCommentId = commentRequest.getParentId(); //je prend l'ID du comment parent
        if(parentCommentId  != null) {
            parentComment = commentRepository.findById(parentCommentId)
                                             .orElseThrow(() -> new RuntimeException("Parent Comment Not Found"));

            if(!parentComment.getPost().getPostId().equals(post.getPostId())){
                throw new RuntimeException("Parent comment must belong to the same post");
            }
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setContent(commentRequest.getContent());
        comment.setParent(parentComment);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return new CommentResponseDTO(comment);

    }

    public List<CommentResponseDTO> getAllCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new RuntimeException("Post with Id " + postId + " Not Found"));

        // tous les commentaires racines cad ceux avec parentComment = null.. c'est le this.parentId = null dans CommentResponseDTO
        List<Comment> rootComments = commentRepository.findByPost_PostIdAndParentIsNullOrderByCreatedAt(post.getPostId());

        return rootComments.stream()
                           .map((comment) -> mapToDoWithReplies(comment))
                           .toList();
    }

    public void delete(Long id, String userEmail) {
        Comment commentToDelete = commentRepository.findById(id)
                                                   .orElseThrow(() -> new RuntimeException("comment with Id " + id + " not Found"));

        if(!commentToDelete.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("Can only delete your own comment");
        }
        commentRepository.delete(commentToDelete);
    }

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
    }

    @Transactional
    public CommentResponseDTO updateComment(Long id, String email, CreateCommentDTO comment) throws AccessDeniedException {
        Comment commentToUpdate = commentRepository.findById(id)
                                                   .orElseThrow(() -> new EntityNotFoundException("Comment with Id " + id + " Not Found"));

        if(!commentToUpdate.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException("You can only update yur own comment");
        }
        commentToUpdate.setContent(comment.getContent());

        return new CommentResponseDTO(commentToUpdate);
    }


    /*-------------------------- Classe privée -----------------------------------------*/
    private CommentResponseDTO mapToDoWithReplies(Comment comment) {
        CommentResponseDTO commentPrincipal = new CommentResponseDTO(comment);

        //trouver les réponses directes à un commentaire
        List<Comment> directReplies = commentRepository.findByParent_CommentIdOrderByCreatedAtAsc(comment.getCommentId());

        List<CommentResponseDTO> repliesDTO = directReplies.stream()
                                                           .map((com) -> mapToDoWithReplies(com))
                                                           .toList();
        commentPrincipal.setReplies(repliesDTO);

        return commentPrincipal;
    }


}
