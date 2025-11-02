import { Component, computed, EventEmitter, inject, Input, Output, signal } from "@angular/core";
import { Post } from "../../models/post.model";
import { CommonModule } from "@angular/common";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatMenuModule } from "@angular/material/menu";
import { Router, RouterLink } from "@angular/router";
import { VoteType } from "../../models/vote.model";
import { PostService } from "../../services/PostService/post.service";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { VoteService } from "../../services/VoteService/vote.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { MatDialog } from "@angular/material/dialog";

export interface VoteEvent {
	postId: number;
	voteType: VoteType;
}

export interface PostActionEvent {
	post: Post;
}

@Component({
	selector: "app-post-card",
	standalone: true,
	imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatMenuModule, RouterLink],
	templateUrl: "./post-card.component.html",
	styleUrl: "./post-card.component.scss",
})
export class PostCardComponent {
	@Input() post!: Post;
	@Input() canDo = false;

	@Output() voteChanged = new EventEmitter<VoteEvent>();
	@Output() editRequested = new EventEmitter<PostActionEvent>();
	@Output() deleteRequested = new EventEmitter<PostActionEvent>();

	private authService = inject(AuthServiceService);
	private snackbar = inject(MatSnackBar);

	VoteType = VoteType;
	userVote = signal<VoteType | null>(null);

	/*
	private router = inject(Router);
	private postService = inject(PostService);
	private authService = inject(AuthServiceService);
	private voteService = inject(VoteService);
	private snackBar = inject(MatSnackBar);
	private dialog = inject(MatDialog); */

	trunctateContent = computed(() => {
		if (!this.post.content) return "";
		const maxLength = 200;
		return this.post.content.length > maxLength
			? this.post.content.substring(0, maxLength) + "..."
			: this.post.content;
	});

	/*
	onDelete(): void {
		this.delete.emit(this.post);
	}

	onEdit(): void {
		this.edit.emit(this.post);
	} */

	onVote(voteType: VoteType): void {
		if (!this.authService.isLoggedIn()) {
			this.snackbar.open("Vous devez être connecté pour voter", "OK", { duration: 3000 });
			return;
		}

		if (this.userVote() === voteType) {
			this.userVote.set(null);
		} else {
			this.userVote.set(voteType);
		}

		this.voteChanged.emit({
			postId: this.post.id,
			voteType: voteType,
		});

		/*this.voteService.votePost(this.post.id, { voteType }).subscribe({
			next: () => {
				if (this.userVote() === voteType) {
					this.userVote.set(null);
					this.post.voteCount -= voteType === VoteType.UPVOTE ? 1 : -1; //decrement vote
				} else {
					const previousVote = this.userVote();
					this.userVote.set(voteType);

					if (previousVote === null) {
						this.post.voteCount += voteType === VoteType.UPVOTE ? 1 : -1;
					} else {
						this.post.voteCount += voteType === VoteType.UPVOTE ? 2 : -2;
					}
				}

				this.postService.refreshPosts();
			},
			error: (error) => {
				console.error("Error voting: ", error);
				this.snackBar.open("Erreur lors du vote", "OK", { duration: 3000 });
			},
		});*/
	}

	getRealTime(date: Date): string {
		const now = new Date();
		const diffInSeconds = Math.floor((now.getTime() - new Date(date).getTime()) / 1000);

		if (diffInSeconds < 60) return `A l'instant`;
		if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}m`;
		if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)}m`;
		if (diffInSeconds < 2592000) return `${Math.floor(diffInSeconds / 86400)}m`;
		if (diffInSeconds < 31536000) return `${Math.floor(diffInSeconds / 2592000)}m`;

		return `${Math.floor(diffInSeconds / 31536000)}an`;
	}

	onImageClick(): void {
		if (this.post.imageUrl) {
			window.open(this.post.imageUrl, "_blank");
		}
	}

	onShare() {
		if (navigator.share) {
			navigator.share({
				title: this.post.title,
				url: `${window.location.origin}/post/${this.post.id}`,
			});
		} else {
			navigator.clipboard.writeText(`${window.location.origin}/post/${this.post.id}`);
		}
	}

	canEditPost(): boolean {
		const currentUser = this.authService.currentUser();
		if (!currentUser) return false;
		return currentUser.id === this.post.userId;
	}

	onDelete(): void {
		if (!this.canEditPost()) {
			this.snackbar.open("Vous n'êtes pas autorisé à supprimer ce post", "OK", { duration: 1500 });
			return;
		}

		const confirmeDelete = confirm(
			`Êtes vous sur de vouloir supprimer le post ${this.post.title} ?`
		);

		if (confirmeDelete) {
			this.deleteRequested.emit({ post: this.post });
			/*this.postService.deletePost(this.post.id).subscribe({
				next: () => {
					this.snackBar.open("Vous n'êtes pas autorisé à supprimer ce post", "OK", {
						duration: 3000,
					});
				},
				error: (error) => {
					console.error("Error deleting post:", error);
					this.snackBar.open("Erreur lors de la suppression du post", "OK", { duration: 3000 });
				},
			});*/
		}
	}

	onEdit(): void {
		if (!this.canEditPost()) {
			this.snackbar.open("Vous n'êtes pas autorisé à modifier ce post", "OK", { duration: 1500 });
			return;
		}

		this.editRequested.emit({ post: this.post });
	}
}
