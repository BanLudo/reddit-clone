import { CommonModule } from "@angular/common";
import { Component, inject, OnInit, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
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
export class LoginComponent implements OnInit {
	private fb = inject(FormBuilder);
	private router = inject(Router);
	private activatedRoute = inject(ActivatedRoute);
	private snackBar = inject(MatSnackBar);
	private authService = inject(AuthServiceService);

	private returnUrl: string = "/";

	//signals
	loading = signal<boolean>(false);
	hidePassword = signal<boolean>(true);

	loginForm = this.fb.group({
		email: ["", [Validators.required, Validators.email]],
		password: ["", [Validators.required]],
	});

	ngOnInit(): void {
		if (this.authService.isLoggedIn()) {
			//redirige si déjà connecté.
			this.router.navigate(["/"]);
			return;
		}

		this.returnUrl = this.activatedRoute.snapshot.queryParams["returnUrl"] || "/"; //url de retour depuis les query params
	}

	onSubmit(): void {
		if (this.loginForm.valid) {
			this.loading.set(true);

			const loginRequest: AuthRequest = this.loginForm.value as AuthRequest;

			this.authService.login(loginRequest).subscribe({
				next: () => {
					this.loading.set(false);
					this.router.navigateByUrl(this.returnUrl);
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
