import { CommonModule } from "@angular/common";
import { Component, computed, inject, OnInit, signal } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { Post } from "../../models/post.model";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { MatDividerModule } from "@angular/material/divider";
import { CommentFormComponent } from "../comment-form/comment-form.component";
import { CommentListComponent } from "../comment-list/comment-list.component";
import { VoteType } from "../../models/vote.model";
import { CommentService } from "../../services/CommentService/comment.service";
import { VoteService } from "../../services/VoteService/vote.service";
import { PostService } from "../../services/PostService/post.service";
import { ActivatedRoute, RouterModule } from "@angular/router";
import { Comment } from "../../models/comment.model";

@Component({
	selector: "app-post-detail",
	standalone: true,
	imports: [
		CommonModule,
		MatCardModule,
		MatButtonModule,
		MatIconModule,
		MatProgressSpinnerModule,
		MatDividerModule,
		CommentFormComponent,
		CommentListComponent,
		RouterModule,
	],
	templateUrl: "./post-detail.component.html",
	styleUrl: "./post-detail.component.scss",
})
export class PostDetailComponent implements OnInit {
	authService = inject(AuthServiceService);
	private commentService = inject(CommentService);
	private voteService = inject(VoteService);
	private postService = inject(PostService);
	private route = inject(ActivatedRoute);

	VoteType = VoteType;

	//signal
	loading = signal<boolean>(true);
	post = signal<Post | null>(null);
	postId = signal<number>(0);
	userVote = signal<VoteType | null>(null);

	//computed
	comments = computed(() => {
		const id = this.postId();
		return this.commentService.comments()[id] || [];
	});

	ngOnInit(): void {
		const id = Number(this.route.snapshot.paramMap.get("id"));
		this.postId.set(id);

		this.loadPost(id);
		this.loadComments(id);
	}

	getRelativeTime(date: Date) {
		const now = new Date();
		const diffInSeconds = Math.floor((now.getTime() - new Date(date).getTime()) / 1000);

		if (diffInSeconds < 60) return `A l'instant`;
		if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}m`;
		if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)}m`;
		if (diffInSeconds < 2592000) return `${Math.floor(diffInSeconds / 86400)}m`;
		if (diffInSeconds < 31536000) return `${Math.floor(diffInSeconds / 2592000)}m`;

		return `${Math.floor(diffInSeconds / 31536000)}an`;
	}

	onVote(voteType: VoteType) {
		if (this.authService.isLoggedIn() && this.post()) {
			this.voteService.votePost(this.post()!.id, { voteType }).subscribe({
				next: () => {
					if (this.userVote() === voteType) {
						this.userVote.set(null);
					} else {
						this.userVote.set(voteType);
					}

					this.loadPost(this.postId());
				},
			});
		}
	}

	onCommentAdded(comment: Comment) {
		this.loadComments(this.postId());

		if (this.post()) {
			this.post.update((post) => (post ? { ...post, commentCount: post.commentCount + 1 } : null));
		}
	}

	private loadPost(id: number) {
		this.postService.getPostById(id).subscribe({
			next: (post) => {
				this.post.set(post);
				this.loading.set(false);
			},
			error: (error) => {
				this.loading.set(false);
				console.error("Error loading post: ", error);
			},
		});
	}

	private loadComments(postId: number) {
		this.commentService.getCommentsByPostId(postId).subscribe({
			error: (error) => {
				console.error("Error loading comments: ", error);
			},
		});
	}
}
