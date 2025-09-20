import { CommonModule } from "@angular/common";
import { Component, inject, signal } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";
import { Router, RouterModule } from "@angular/router";
import { PostService } from "../../services/PostService/post.service";
import { PostRequest } from "../../models/post.model";

@Component({
	selector: "app-create-post",
	standalone: true,
	imports: [
		CommonModule,
		ReactiveFormsModule,
		MatCardModule,
		MatInputModule,
		MatFormFieldModule,
		MatButtonModule,
		MatProgressSpinnerModule,
		RouterModule,
		MatIconModule,
	],
	templateUrl: "./create-post.component.html",
	styleUrl: "./create-post.component.scss",
})
export class CreatePostComponent {
	private fb = inject(FormBuilder);
	private postService = inject(PostService);
	private snackbar = inject(MatSnackBar);
	private router = inject(Router);

	loadingSignal = signal<boolean>(false);
	uploadingImageSignal = signal<boolean>(false);
	selectedImageUrlSignal = signal<string | null>(null);

	postForm: FormGroup = this.fb.group({
		title: ["", [Validators.required, Validators.maxLength(300)]],
		content: [""],
		imageUrl: [""],
	});

	onSubmit(): void {
		if (this.postForm.valid) {
			this.loadingSignal.set(true);

			const postRequest: PostRequest = {
				title: this.postForm.value.title,
				content: this.postForm.value.content || undefined,
				imageUrl: this.postForm.value.imageUrl || undefined,
			};

			this.postService.createPost(postRequest).subscribe({
				next: (post) => {
					this.loadingSignal.set(false);
					this.snackbar.open("Post crée avec succès !", "OK", { duration: 3000 });
					this.router.navigate(["/post", post.id]);
				},
				error: (error) => {
					this.loadingSignal.set(false);
					this.snackbar.open("Erreur lors de la création du post", "OK", { duration: 3000 });
					console.error("Error creating post:", error);
				},
			});
		}
	}

	removeImage() {
		this.selectedImageUrlSignal.set(null);
		this.postForm.patchValue({ imageUrl: "" });
	}

	onFileSelected(event: any) {}
}
