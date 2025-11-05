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
	],
	templateUrl: "./profile.component.html",
	styleUrl: "./profile.component.scss",
})
export class ProfileComponent implements OnInit {
	loading = signal(true);
	loadingPosts = signal(false);
	currentUser = signal<User | null>(null);
	userPosts = signal<Post[]>([]);
	currentPostPage = signal(0);
	totalPostPages = signal(0);

	private authService = inject(AuthServiceService);
	private postService = inject(PostService);

	//computed
	hasMorePosts = computed(() => {
		return this.currentPostPage() < this.totalPostPages() - 1;
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
					this.currentUser.set(user);
					this.loading.set(false);
					if (user) {
						this.loadUserPosts();
					}
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

	onTabChange(event: any): void {
		const tabIndex = event.index;

		if (tabIndex === 0) {
			//Tab "mes posts" - déjà chargé
		} else if (tabIndex === 1) {
			// Tab 'Mes commentaires' @faire
			console.log("Load user comments");
		} else if (tabIndex === 2) {
			//Tab "Posts sauvegardé" @faire
			console.log("Load saved posts");
		}
	}
} // fin
