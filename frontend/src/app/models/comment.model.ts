import { Post } from "./post.model";
import { User } from "./user.model";

export interface Comment {
	id: number;
	content: string;
	author: User;
	parentId: number;
	postId: number;
	createdAt: Date;
	updatedAt: Date;
	replies: Comment[];
}

export interface CommentRequest {
	content: string;
	parentId?: number;
}
