import { CommonModule } from "@angular/common";
import { Component, inject, OnInit, signal } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { PostService } from "../../services/PostService/post.service";
import { Post, PostRequest } from "../../models/post.model";

@Component({
	selector: "app-edit-post",
	standalone: true,
	imports: [
		MatProgressSpinnerModule,
		CommonModule,
		ReactiveFormsModule,
		RouterModule,
		MatCardModule,
		MatFormFieldModule,
		MatInputModule,
		MatButtonModule,
		MatIconModule,
		MatSnackBarModule,
	],
	templateUrl: "./edit-post.component.html",
	styleUrl: "./edit-post.component.scss",
})
export class EditPostComponent implements OnInit {
	private fb = inject(FormBuilder);
	private router = inject(Router);
	private route = inject(ActivatedRoute);
	private snackBar = inject(MatSnackBar);
	private postService = inject(PostService);

	loading = signal(true);
	savingPost = signal(false);
	uploadingImage = signal(false);
	selectedImageUrl = signal<String | null>(null);
	postId = signal<number>(0);
	originalPost = signal<Post | null>(null);

	postForm: FormGroup = this.fb.group({
		title: ["", [Validators.required, Validators.maxLength(300)]],
		content: [""],
		imageUrl: [""],
	});

	ngOnInit(): void {
		const id = Number(this.route.snapshot.paramMap.get("id"));
		this.postId.set(id);
		this.loadPost(id);
	}

	private loadPost(id: number): void {
		this.postService.getPostById(id).subscribe({
			next: (post) => {
				this.originalPost.set(post);

				this.postForm.patchValue({
					title: post.title,
					content: post.content || "",
					imageUrl: post.imageUrl || "",
				});

				if (post.imageUrl) {
					this.selectedImageUrl.set(post.imageUrl);
				}
				this.loading.set(false);
			},
			error: (error) => {
				console.error("Error loading post:", error);
				this.snackBar.open("Erreur lors du chargement du post", "OK", { duration: 3000 });
				this.router.navigate(["/"]);
			},
		});
	}

	onSubmit(): void {
		if (this.postForm.valid && !this.savingPost()) {
			this.savingPost.set(true);

			const postRequest: PostRequest = {
				title: this.postForm.value.title,
				content: this.postForm.value.content || undefined,
				imageUrl: this.postForm.value.imageUrl || undefined,
			};

			this.postService.updatePost(this.postId(), postRequest).subscribe({
				next: (post) => {
					this.savingPost.set(false);
					this.snackBar.open("Post modifié avec succès !", "OK", { duration: 3000 });
					this.router.navigate(["/post", post.id]);
				},
				error: (error) => {
					this.savingPost.set(false);
					this.snackBar.open("Erreur lors de la modification du post", "OK", { duration: 3000 });
					console.error("Error updating post:", error);
				},
			});
		}
	}

	onFileSelected(event: any): void {}

	removeImage(): void {
		this.selectedImageUrl.set(null);
		this.postForm.patchValue({
			imageUrl: "",
		});
	}
}
