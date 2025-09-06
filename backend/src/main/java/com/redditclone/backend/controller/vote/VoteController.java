package com.redditclone.backend.controller.vote;

import com.redditclone.backend.DTO.vote.VoteRequest;
import com.redditclone.backend.service.UserPrincipal;
import com.redditclone.backend.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }


    @PostMapping("/posts/{postId}/vote")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> votePost(@PathVariable Long postId, @RequestBody VoteRequest voteRequest, @AuthenticationPrincipal UserPrincipal currentUser){
        voteService.votePost(postId, voteRequest, currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{commentId}/vote")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> voteComment(@PathVariable Long commentId, @RequestBody VoteRequest voteRequest, @AuthenticationPrincipal UserPrincipal currentUser){
        voteService.voteComment(commentId, voteRequest, currentUser);
        return ResponseEntity.ok().build();
    }

}
