import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSnackBar } from "@angular/material/snack-bar";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { Router, RouterLink } from "@angular/router";

@Component({
	selector: "app-signup",
	standalone: true,
	imports: [
		CommonModule,
		ReactiveFormsModule,
		MatButtonModule,
		MatInputModule,
		MatProgressSpinnerModule,
		MatFormFieldModule,
		MatCardModule,
		RouterLink,
	],
	templateUrl: "./signup.component.html",
	styleUrl: "./signup.component.scss",
})
export class SignupComponent {
	private fb = inject(FormBuilder);
	private snackBar = inject(MatSnackBar);
	private router = inject(Router);
	authService = inject(AuthServiceService);

	signupForm = this.fb.group({
		username: ["", [Validators.required, Validators.minLength(3)]],
		email: ["", [Validators.required, Validators.email]],
		password: ["", [Validators.required, Validators.minLength(6)]],
	});

	onSubmit(): void {
		this.authService.register(this.signupForm.value as any).subscribe({
			next: () => {
				this.router.navigate(["/"]);
				this.snackBar.open("Account created successfully", "Close", { duration: 3000 });
			},
			error: (error) => {
				let message = "Registration failed. Try again";
				if (error.error && typeof error.error == "string") {
					message = error.error;
				}
				this.snackBar.open(message, "Close", {
					duration: 5000,
				});
			},
		});
	}
}
