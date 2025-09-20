import { CommonModule } from "@angular/common";
import { Component, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { Router, RouterModule } from "@angular/router";
import { MatButton, MatAnchor } from "@angular/material/button";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatInputModule } from "@angular/material/input";
import { MatSnackBar } from "@angular/material/snack-bar";
import { AuthRequest } from "../../models/auth.model";
import { MatIconModule } from "@angular/material/icon";

@Component({
	selector: "app-login",
	standalone: true,
	imports: [
		CommonModule,
		MatCardModule,
		ReactiveFormsModule,
		MatFormFieldModule,
		MatButton,
		MatInputModule,
		MatIconModule,
		MatProgressSpinnerModule,
		MatAnchor,
		RouterModule,
	],
	templateUrl: "./login.component.html",
	styleUrl: "./login.component.scss",
})
export class LoginComponent {
	private fb = inject(FormBuilder);
	private router = inject(Router);
	private snackBar = inject(MatSnackBar);
	private authService = inject(AuthServiceService);

	//signals
	loading = signal<boolean>(false);
	hidePassword = signal<boolean>(true);

	loginForm = this.fb.group({
		email: ["", [Validators.required, Validators.email]],
		password: ["", [Validators.required]],
	});

	onSubmit(): void {
		if (this.loginForm.valid) {
			this.loading.set(true);

			const loginRequest: AuthRequest = this.loginForm.value as AuthRequest;

			this.authService.login(loginRequest).subscribe({
				next: () => {
					this.loading.set(false);
					this.router.navigate(["/"]);
					this.snackBar.open("Login successful", "Close", { duration: 3000 });
				},
				error: (error) => {
					this.loading.set(false);
					this.snackBar.open("Login failed. Please check credentials.", "Close", {
						duration: 5000,
					});
				},
			});
		}
	}

	togglePasswordVisibility(): void {
		this.hidePassword.update((value) => !value);
	}
}
