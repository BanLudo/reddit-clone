export interface User {
	id: number;
	username: string;
	email: string;
	UserProfile?: string;
	createdAt: Date;
	imageUrl?: string;

	postCount?: number;
	commentCount?: number;
}
