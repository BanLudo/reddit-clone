import { CommonModule } from "@angular/common";
import { Component, computed, effect, inject, OnInit, signal } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSelectModule } from "@angular/material/select";
import { RouterModule } from "@angular/router";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { PostService } from "../../services/PostService/post.service";
import { PostCardComponent } from "../post-card/post-card.component";
import { VoteType } from "../../models/vote.model";
import { VoteService } from "../../services/VoteService/vote.service";

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
	voteService = inject(VoteService);

	sortBy = signal<string>("new");

	hasMorePosts = computed(() => {
		return this.postService.currentPage() < this.postService.totalPage() - 1;
	});

	constructor() {
		this.loadPosts(this.sortBy()); //1ere load
	}

	ngOnInit(): void {
		this.postService.getAllPosts(0, 10, this.sortBy()).subscribe();
	}

	loadMorePosts() {
		const nextPage = this.postService.currentPage() + 1;
		this.postService.getAllPosts(nextPage, 10, this.sortBy()).subscribe();
	}

	onVote(postId: number, voteType: VoteType): void {
		if (this.authService.isAuthenticated()) {
			this.voteService.votePost(postId, { voteType }).subscribe({
				next: () => this.postService.refreshPosts(),
				error: (error: any) => console.error("Error voting:", error),
			});
		}
	}

	onSortChange(sort: string): void {
		this.sortBy.set(sort);
		this.loadPosts(sort);
	}

	private loadPosts(sort: string) {
		this.postService.getAllPosts(0, 10, sort).subscribe();
	}
}
