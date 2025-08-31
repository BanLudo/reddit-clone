package com.redditclone.backend.controller.vote;

import com.redditclone.backend.DTO.vote.VoteDto;
import com.redditclone.backend.DTO.vote.VoteReponse;
import com.redditclone.backend.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<VoteReponse> vote(@RequestBody VoteDto voteDto, Authentication authentication){
        String userEmail = authentication.getName();
        voteService.vote(voteDto, userEmail);

        int updatedScore = voteService.getScore(voteDto);
        return ResponseEntity.ok(new VoteReponse(updatedScore));
    }

}
