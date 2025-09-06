package com.redditclone.backend.DTO.vote;

import com.redditclone.backend.enumeration.VoteType;
import jakarta.validation.constraints.NotNull;

public class VoteRequest{

    @NotNull
    private VoteType voteType;

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }


}
