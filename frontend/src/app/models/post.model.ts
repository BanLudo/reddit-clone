import { Comment } from "./comment.model";
import { User } from "./user.model";

export interface Post {
	id: number;
	title: string;
	content?: string;
	imageUrl?: string;
	user: User;
	voteCount: number;
	createdAt: Date;
	updatedAt: Date;
	comments: Comment[];
}

export interface PostRequest {
	title: string;
	content?: string;
	imageUrl?: string;
}

export interface PostPage {
	content: Post[];
	totalElements: number;
	totalPages: number;
	size: number;
	number: number;
	first: boolean;
	last: boolean;
}
