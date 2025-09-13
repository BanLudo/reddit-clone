export enum VoteType {
	UPVOTE = "UPVOTE",
	DOWNVOTE = "DOWNVOTE",
}

export interface VoteRequest {
	voteType: VoteType;
}
