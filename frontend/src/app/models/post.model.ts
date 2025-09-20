import { Comment } from "./comment.model";
import { User } from "./user.model";

export interface Post {
	id: number;
	title: string;
	content?: string;
	imageUrl?: string;
	username: string;
	userId: number;
	voteCount: number;
	commentCount: number;
	createdAt: Date;
	updatedAt: Date;

	/*user: User;*/
	/*comments: Comment[]; autre methode */
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
	/*first: boolean;
	last: boolean; autre methode */
}
