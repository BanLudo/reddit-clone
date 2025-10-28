import { CommonModule } from "@angular/common";
import { Component, EventEmitter, inject, Input, OnInit, Output, signal } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { Comment, CommentRequest } from "../../models/comment.model";
import { CommentService } from "../../services/CommentService/comment.service";

@Component({
	selector: "app-comment-form",
	standalone: true,
	imports: [
		CommonModule,
		MatCardModule,
		MatFormFieldModule,
		MatInputModule,
		MatButtonModule,
		MatProgressSpinnerModule,
		ReactiveFormsModule,
	],
	templateUrl: "./comment-form.component.html",
	styleUrl: "./comment-form.component.scss",
})
export class CommentFormComponent {
	@Input() parentId?: number;
	@Input() postId!: number;
	@Input() commentId!: number;
	@Input() isEditMode: boolean = false;
	@Input() initialContent: string = "";

	@Output() commentAdded = new EventEmitter<Comment>();
	@Output() cancelledComment = new EventEmitter<void>();
	@Output() commentUpdated = new EventEmitter<Comment>();

	private fb = inject(FormBuilder);
	private commentService = inject(CommentService);

	commentForm: FormGroup = this.fb.group({
		content: ["", [Validators.required, Validators.minLength(1)]],
	});

	//signal
	loadingSignal = signal<boolean>(false);

	onSubmit(): void {
		if (this.commentForm.valid) {
			this.loadingSignal.set(true);

			const commentRequest: CommentRequest = {
				content: this.commentForm.value.content,
				parentId: this.parentId,
			};

			this.commentService.createComment(this.postId, commentRequest).subscribe({
				next: (comment) => {
					this.loadingSignal.set(false);
					this.commentForm.reset();
					this.commentAdded.emit(comment);
				},
				error: (error) => {
					this.loadingSignal.set(false);
					console.error("Error creating comment:", error);
				},
			});
		}
	}

	onCancel(): void {
		this.commentForm.reset();
		this.cancelledComment.emit();
	}
}
