import { CommonModule } from "@angular/common";
import { Component, inject, Input, signal } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { MatMenuModule } from "@angular/material/menu";
import { Comment, CommentRequest } from "../../models/comment.model";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { VoteService } from "../../services/VoteService/vote.service";
import { CommentService } from "../../services/CommentService/comment.service";
import { CommentFormComponent } from "../comment-form/comment-form.component";
import { VoteType } from "../../models/vote.model";
import {
	FormBuilder,
	FormGroup,
	Validators,
	ɵInternalFormsSharedModule,
	ReactiveFormsModule,
} from "@angular/forms";
import { MatFormField, MatLabel, MatError } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";
import { MatProgressSpinner } from "@angular/material/progress-spinner";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
	selector: "app-comment-item",
	standalone: true,
	imports: [
		CommonModule,
		MatCardModule,
		MatButtonModule,
		MatIconModule,
		MatMenuModule,
		CommentFormComponent,
		ɵInternalFormsSharedModule,
		ReactiveFormsModule,
		MatFormField,
		MatLabel,
		MatInput,
		MatError,
		MatProgressSpinner,
	],
	templateUrl: "./comment-item.component.html",
	styleUrl: "./comment-item.component.scss",
})
export class CommentItemComponent {
	@Input() comment!: Comment;
	@Input() postId!: number;
	@Input() canDo = false;
	@Input() level = 0;

	authService = inject(AuthServiceService);
	private voteService = inject(VoteService);
	private commentService = inject(CommentService);
	private fb = inject(FormBuilder);
	private snackBar = inject(MatSnackBar);

	VoteType = VoteType;

	//signal
	showReply = signal<boolean>(false);
	userVote = signal<VoteType | null>(null);
	isEditing = signal<boolean>(false);
	editLoading = signal<boolean>(false);

	//Form pour l'edition
	editForm: FormGroup = this.fb.group({
		content: ["", [Validators.required, Validators.minLength(1)]],
	});

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

	onDelete(): void {
		if (confirm("Etes vous sur de vouloir supprimer ce commentaire ?")) {
			this.commentService.deleteComment(this.comment.id, this.postId).subscribe({
				next: () => {
					this.snackBar.open("Commentaire delete success", "Close", {
						duration: 2000,
					});
				},
				error: (error) => {
					this.snackBar.open("Error deleting comment", "Close", {
						duration: 2000,
					});
					console.error("Error deleting comment : ", error);
				},
			});
		}
	}

	onVote(voteType: VoteType) {
		if (this.authService.isLoggedIn()) {
			this.voteService.voteComment(this.comment.id, { voteType }).subscribe({
				next: () => {
					if (this.userVote() === voteType) {
						this.userVote.set(null);
					} else {
						this.userVote.set(voteType);
					}
				},
				error: (error) => {
					this.snackBar.open("Error voting on comment", "Close", {
						duration: 2000,
					});
					console.error("Error voting on comment : ", error);
				},
			});
		}
	}

	toggleReply() {
		this.showReply.update((value) => !value);
	}

	onReplyAdded(reply: Comment) {
		this.showReply.set(false);

		this.commentService.commentsSignal.update((commentsMap) => ({
			...commentsMap,
			[this.postId]: [reply, ...(commentsMap[this.postId] || [])],
		}));
	}

	//-----------------EDITION ----------------
	canEditComment(): boolean {
		const currentUser = this.authService.currentUser();
		if (!currentUser) return false;

		return currentUser.id === this.comment.userId; //on ne modifie que nos propres commentaires.
	}

	onCancelEdit(): void {
		this.isEditing.set(false);
		this.editForm.reset();

		this.editForm.patchValue({
			content: this.comment.content,
		});
	}

	onEdit(): void {
		if (!this.canEditComment()) {
			console.warn("Non autorisé à modifier ce commentaire");
			return;
		}

		this.isEditing.set(true);

		this.editForm.patchValue({
			content: this.comment.content,
		});
	}

	onSaveEdit(): void {
		if (this.editForm.valid && !this.editLoading()) {
			this.editLoading.set(true);

			const commentRequest: CommentRequest = {
				content: this.editForm.value.content.trim(),
			};

			//verif si le contenu a changé
			if (commentRequest.content === this.comment.content) {
				this.onCancelEdit();
				return;
			}

			this.commentService.updateComment(this.comment.id, commentRequest).subscribe({
				next: (updatedComment) => {
					this.comment = { ...this.comment, ...updatedComment }; //mise à jour locale
					this.editLoading.set(false);
					this.isEditing.set(false);
					this.snackBar.open("Commentaire mis à jours avec succes", "Close", { duration: 2000 });
				},
				error: (error) => {
					this.editLoading.set(false);
					this.snackBar.open("Error lors de la mise à jour du commentaire", "Close", {
						duration: 4000,
					});
					console.error("Error lors de la mise à jour du commentaire", error);
				},
			});
		}
	}
}
