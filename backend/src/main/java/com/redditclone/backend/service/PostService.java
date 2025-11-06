package com.redditclone.backend.service;

import com.redditclone.backend.DTO.PostRequest;
import com.redditclone.backend.DTO.PostResponse;
import com.redditclone.backend.model.Post;
import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.PostRepository;
import com.redditclone.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public Page<PostResponse> getAllPosts(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts;

        if("popular".equals(sort)) {
            posts = postRepository.findAllOrderByVoteCountDesc(pageable);
        }else {
            posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return posts.map(this::mapToPostResponse);
    }


    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                                  .orElseThrow(()-> new RuntimeException("Post not found"));

        return mapToPostResponse(post);
    }


    public PostResponse createdPost(UserPrincipal currentUser, @Valid PostRequest postRequest) {
        User user  = userRepository.findById(currentUser.getId())
                                             .orElseThrow(()-> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setImageUrl(postRequest.getImageUrl());
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return mapToPostResponse(savedPost);
    }


    public PostResponse updatePost(Long id, PostRequest postRequest, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                                  .orElseThrow(()-> new RuntimeException("Post not found"));

        //eviter de uptade un post qui n'est pas le sien
        if(!post.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("No permission to edit this post");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setImageUrl(postRequest.getImageUrl());
        Post updatedPost = postRepository.save(post);

        return mapToPostResponse(updatedPost);
    }


    public void deletePost(Long id, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Post not found"));

        //eviter de uptade un post qui n'est pas le sien
        if(!post.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("Can only delete your own post");
        }

        postRepository.delete(post);
    }


    public Page<PostResponse> getPostsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable);

        return posts.map(this::mapToPostResponse);
    }

    public Long countUserPosts(Long userId) {
        return postRepository.countByUser_Id(userId);
    }


    private PostResponse mapToPostResponse(Post post) {
        PostResponse postResponse = new PostResponse();

        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setImageUrl(post.getImageUrl());
        postResponse.setUsername(post.getUser().getUsername());
        postResponse.setUserId(post.getUser().getId());
        postResponse.setVoteCount(post.getVoteCount());
        postResponse.setCommentCount(post.getComments().size());
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setUpdatedAt(post.getUpdatedAt());

        return postResponse;
    }

}
