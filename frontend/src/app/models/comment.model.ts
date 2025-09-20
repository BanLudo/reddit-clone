import { Post } from "./post.model";
import { User } from "./user.model";

export interface Comment {
	id: number;
	content: string;
	/*author: User;*/
	username: string;
	userId: number;
	voteCount: number;
	/*parentId: number;
	postId: number;*/
	createdAt: Date;
	updatedAt: Date;
	parentId?: number;
	replies: Comment[];
}

export interface CommentRequest {
	content: string;
	parentId?: number;
}
