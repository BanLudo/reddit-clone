import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { Router, RouterLink } from "@angular/router";
import { MatButton, MatAnchor } from "@angular/material/button";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatInputModule } from "@angular/material/input";
import { MatSnackBar } from "@angular/material/snack-bar";

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
		MatProgressSpinnerModule,
		MatAnchor,
		RouterLink,
	],
	templateUrl: "./login.component.html",
	styleUrl: "./login.component.scss",
})
export class LoginComponent {
	private fb = inject(FormBuilder);
	private router = inject(Router);
	private snackBar = inject(MatSnackBar);
	authService = inject(AuthServiceService);

	loginForm = this.fb.group({
		username: ["", Validators.required],
		password: ["", Validators.required],
	});

	onSubmit(): void {
		if (this.loginForm.valid) {
			this.authService.login(this.loginForm.value as any).subscribe({
				next: () => {
					this.router.navigate(["/"]);
					this.snackBar.open("Login successful", "Close", { duration: 3000 });
				},
				error: (error) => {
					this.snackBar.open("Login failed. Please check credentials.", "Close", {
						duration: 5000,
					});
				},
			});
		}
	}
}
