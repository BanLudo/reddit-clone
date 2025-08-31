package com.redditclone.backend.DTO.vote;

public class VoteReponse {
    private int score;

    public VoteReponse(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
