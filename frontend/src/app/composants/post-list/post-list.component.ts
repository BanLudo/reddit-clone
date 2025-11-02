import { CommonModule } from "@angular/common";
import { Component, computed, effect, inject, OnInit, signal } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSelectModule } from "@angular/material/select";
import { Router, RouterModule } from "@angular/router";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { PostService } from "../../services/PostService/post.service";
import { PostActionEvent, PostCardComponent, VoteEvent } from "../post-card/post-card.component";
import { VoteType } from "../../models/vote.model";
import { VoteService } from "../../services/VoteService/vote.service";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
	selector: "app-post-list",
	standalone: true,
	imports: [
		CommonModule,
		MatCardModule,
		MatButtonModule,
		MatIconModule,
		MatFormFieldModule,
		MatProgressSpinnerModule,
		RouterModule,
		MatSelectModule,
		PostCardComponent,
	],
	templateUrl: "./post-list.component.html",
	styleUrl: "./post-list.component.scss",
})
export class PostListComponent implements OnInit {
	authService = inject(AuthServiceService);
	postService = inject(PostService);
	private voteService = inject(VoteService);

	private router = inject(Router);
	private snackbar = inject(MatSnackBar);

	sortBy = signal<string>("new");

	hasMorePosts = computed(() => {
		return this.postService.currentPage() < this.postService.totalPage() - 1;
	});

	constructor() {
		//reload when sort changes
		effect(
			() => {
				this.postService.getAllPosts(0, 10, this.sortBy()).subscribe();
			},
			{ allowSignalWrites: true }
		);
	}

	ngOnInit(): void {
		this.postService.getAllPosts(0, 10, this.sortBy()).subscribe();
	}

	// ========================================
	// GESTIONNAIRES D'ÉVÉNEMENTS DU POST-CARD
	// ========================================
	handleVoteChanged(event: VoteEvent): void {
		this.voteService.votePost(event.postId, { voteType: event.voteType }).subscribe({
			next: () => {
				this.postService.refreshPosts();
			},
			error: (error) => {
				console.error("Error voting:", error);
				this.snackbar.open("Erreur lors du vote", "OK", { duration: 3000 });
			},
		});
	}

	handleEditRequested(event: PostActionEvent): void {
		this.router.navigate(["/edit-post", event.post.id]);
	}

	handleDeleteRequested(event: PostActionEvent): void {
		this.postService.deletePost(event.post.id).subscribe({
			next: () => {
				this.snackbar.open("Post supprimé avec succes", "OK", { duration: 3000 });
			},
			error: (error) => {
				console.error("Error deleting post: ", error);
				this.snackbar.open("Erreur suppression du post", "OK", { duration: 3000 });
			},
		});
	}

	// ========================================
	// AUTRES
	// ========================================
	onSortChange(sort: string): void {
		this.sortBy.set(sort);
		this.loadPosts(sort);
	}

	private loadPosts(sort: string) {
		this.postService.getAllPosts(0, 10, sort).subscribe();
	}

	loadMorePosts() {
		const nextPage = this.postService.currentPage() + 1;
		this.postService.getAllPosts(nextPage, 10, this.sortBy()).subscribe();
	}
}
