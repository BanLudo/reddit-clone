import { Component, computed, EventEmitter, Input, Output, signal } from "@angular/core";
import { Post } from "../../models/post.model";
import { CommonModule } from "@angular/common";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatMenuModule } from "@angular/material/menu";
import { RouterLink } from "@angular/router";
import { VoteType } from "../../models/vote.model";

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
	@Output() vote = new EventEmitter<{ postId: number; voteType: VoteType }>();
	@Output() edit = new EventEmitter<Post>();
	@Output() delete = new EventEmitter<Post>();

	VoteType = VoteType;

	userVote = signal<VoteType | null>(null);

	trunctateContent = computed(() => {
		if (!this.post.content) return "";
		const maxLength = 200;
		return this.post.content.length > maxLength
			? this.post.content.substring(0, maxLength) + "..."
			: this.post.content;
	});

	onDelete(): void {
		this.delete.emit(this.post);
	}

	onEdit(): void {
		this.edit.emit(this.post);
	}

	onVote(voteType: VoteType): void {
		this.vote.emit({ postId: this.post.id, voteType });

		if (this.userVote() === voteType) {
			//update du signal vote
			this.userVote.set(null);
		} else {
			this.userVote.set(voteType);
		}
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
}
