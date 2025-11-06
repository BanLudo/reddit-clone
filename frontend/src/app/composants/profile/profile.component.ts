import { CommonModule } from "@angular/common";
import { Component, computed, inject, OnInit, signal } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { User } from "../../models/user.model";
import { Post } from "../../models/post.model";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { PostService } from "../../services/PostService/post.service";
import { MatTabsModule } from "@angular/material/tabs";
import { PostCardComponent } from "../post-card/post-card.component";
import { MatDividerModule } from "@angular/material/divider";
import { RouterModule } from "@angular/router";
import { CommentService } from "../../services/CommentService/comment.service";
import { Comment } from "../../models/comment.model";

@Component({
	selector: "app-profile",
	standalone: true,
	imports: [
		MatCardModule,
		MatIconModule,
		CommonModule,
		MatProgressSpinnerModule,
		MatButtonModule,
		MatTabsModule,
		PostCardComponent,
		MatDividerModule,
		RouterModule,
	],
	templateUrl: "./profile.component.html",
	styleUrl: "./profile.component.scss",
})
export class ProfileComponent implements OnInit {
	private authService = inject(AuthServiceService);
	private postService = inject(PostService);
	private commentService = inject(CommentService);

	loading = signal(true);
	loadingPosts = signal(false);
	loadingComments = signal(false);

	currentUser = signal<User | null>(null);

	//les posts
	userPosts = signal<Post[]>([]);
	currentPostPage = signal(0);
	totalPostPages = signal(0);

	//les commentaires
	userComments = signal<Comment[]>([]);
	currentCommentPage = signal(0);
	totalCommentPages = signal(0);

	//computed
	hasMorePosts = computed(() => {
		return this.currentPostPage() < this.totalPostPages() - 1;
	});

	hasMoreComments = computed(() => {
		return this.currentCommentPage() < this.totalCommentPages() - 1;
	});

	ngOnInit(): void {
		this.loadUserProfile();
	}

	private loadUserProfile(): void {
		const user = this.authService.currentUser();
		if (user) {
			this.currentUser.set(user);
			this.loading.set(false);
			this.loadUserPosts();
		} else {
			this.authService.getCurrentUser().subscribe({
				next: (user) => {
					if (user) {
						this.currentUser.set(user);
						this.loading.set(false);
						this.loadUserPosts();
					}
				},
				error: (error) => {
					console.error("Error loading user profile: ", error);
					this.loading.set(false);
				},
			});
		}
	}

	formatDate(date: Date): string {
		return new Intl.DateTimeFormat("fr-FR", {
			year: "numeric",
			month: "long",
		}).format(new Date(date));
	}

	loadMorePosts(): void {
		const nextPage = this.currentPostPage() + 1;
		this.loadUserPosts(nextPage);
	}

	loadMoreComment(): void {
		const nextPage = this.currentCommentPage() + 1;
		this.loadUserComments(nextPage);
	}

	onTabChange(event: any): void {
		const tabIndex = event.index;

		if (tabIndex === 0) {
			//Tab "mes posts"
			if (this.userPosts().length === 0 && !this.loadingPosts()) {
				this.loadUserPosts(0);
			}
		} else if (tabIndex === 1) {
			// Tab 'Mes commentaires'
			if (this.userComments().length === 0 && !this.loadingComments()) {
				this.loadUserComments(0);
			}
			console.log("Load user comments");
		} else if (tabIndex === 2) {
			//Tab "Posts sauvegardé" @faire
			console.log("Load saved posts - à implementer");
		}
	}

	getRelativeTime(date: Date): string {
		const now = new Date();
		const diffInSeconds = Math.floor((now.getTime() - new Date(date).getTime()) / 1000);

		if (diffInSeconds < 60) return "A l'instant";
		if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}m`;
		if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)}h`;
		if (diffInSeconds < 2592000) return `${Math.floor(diffInSeconds / 86400)}j`;
		if (diffInSeconds < 31536000) return `${Math.floor(diffInSeconds / 2592000)}mois`;
		return `${Math.floor(diffInSeconds / 31536000)}an`;
	}

	/*--------------------------------------------------
	/*				Private class method
	/*--------------------------------------------------*/
	private loadUserComments(page: number): void {
		const user = this.currentUser();

		if (!user) return;

		this.loadingComments.set(true);

		this.commentService.getCommentsByUserId(user.id, page, 10).subscribe({
			next: (response) => {
				if (page === 0) {
					this.userComments.set(response.content);
				} else {
					this.userComments.update((comments) => [...comments, ...response.content]);
				}
				this.loadingComments.set(false);
				this.currentCommentPage.set(response.number);
				this.totalCommentPages.set(response.totalPages);
			},
			error: (error) => {
				this.loadingPosts.set(false);
				console.error("Error loading user posts: ", error);
			},
		});
	}

	private loadUserPosts(page: number = 0): void {
		const user = this.currentUser();

		if (!user) return;

		this.loadingPosts.set(true);

		this.postService.getPostsByUsersId(user.id, page, 10).subscribe({
			next: (response) => {
				if (page === 0) {
					this.userPosts.set(response.content);
				} else {
					this.userPosts.update((posts) => [...posts, ...response.content]);
				}
				this.currentPostPage.set(response.number);
				this.totalPostPages.set(response.totalPages);
				this.loadingPosts.set(false);
			},
			error: (error) => {
				this.loadingPosts.set(false);
				console.error("Error loading user posts: ", error);
			},
		});
	}
} // fin
