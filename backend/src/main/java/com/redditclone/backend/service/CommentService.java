package com.redditclone.backend.service;

import com.redditclone.backend.DTO.comment.CommentResponseDTO;
import com.redditclone.backend.DTO.comment.CreateCommentDTO;
import com.redditclone.backend.model.Comment;
import com.redditclone.backend.model.Post;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.CommentRepository;
import com.redditclone.backend.repository.PostRepository;
import com.redditclone.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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

    /*public void delete(Long id, String userEmail) {
    }

    public CommentRequest updateComment(String userEmail, @Valid CommentRequest commentRequest) {
    }

    public List<CommentRequest> getAllCommentsByUser(Long userId) {
    }

    public List<CommentRequest> getAllCommentsByPost(Long postId) {
    }

    public List<CommentRequest> getAllRepliesByComment(Long commentId) {
    }*/
}
