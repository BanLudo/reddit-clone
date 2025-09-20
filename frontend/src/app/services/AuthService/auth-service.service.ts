import { computed, inject, Injectable, signal } from "@angular/core";
import { User } from "../../models/user.model";
import { ApiResponse, AuthRequest, AuthResponse, RegisterRequest } from "../../models/auth.model";
import { catchError, Observable, tap } from "rxjs";
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";

@Injectable({
	providedIn: "root",
})
export class AuthServiceService {
	private http = inject(HttpClient);
	private router = inject(Router);

	private readonly API_URL = "http://localhost:8080/api/auth";
	private readonly TOKEN_KEY = "auth-token";

	//gestion etats
	private currentUserSignal = signal<User | null>(null);
	private isLoadingSignal = signal<boolean>(false);
	private isLoggedInSignal = signal<boolean>(false);

	//computed signal
	public currentUser = this.currentUserSignal.asReadonly();
	public isLoading = this.isLoadingSignal.asReadonly();
	public isLoggedIn = this.isLoggedInSignal.asReadonly();
	public isAuthenticated = computed(() => this.currentUserSignal() !== null);

	constructor() {
		this.loadUserFromStorage();
	}

	login(credential: AuthRequest): Observable<AuthResponse> {
		//this.isLoadingSignal.set(true);

		return this.http.post<AuthResponse>(`${this.API_URL}/login`, credential).pipe(
			tap((response) => {
				localStorage.setItem(this.TOKEN_KEY, response.token);
				this.currentUserSignal.set(response.user);
				this.isLoggedInSignal.set(true);
				//this.isLoadingSignal.set(false);
			})
		);
	}

	register(registerRequest: RegisterRequest): Observable<ApiResponse> {
		return this.http.post<ApiResponse>(`${this.API_URL}/signup`, registerRequest);
	}

	logout(): void {
		localStorage.removeItem(this.TOKEN_KEY);
		this.isLoggedInSignal.set(false);
		this.currentUserSignal.set(null);
		this.isLoadingSignal.set(false);
		this.router.navigate(["/login"]);
	}

	getCurrentUser(): Observable<User> {
		return this.http.get<User>("http://localhost:8080/api/user/me").pipe(
			tap(() => {
				this.isLoggedInSignal.set(true);
			})
		);
	}

	getToken(): string | null {
		return localStorage.getItem(this.TOKEN_KEY);
	}

	private loadUserFromStorage() {
		const token = localStorage.getItem(this.TOKEN_KEY);
		if (token) {
			this.getCurrentUser().subscribe({
				next: (user) => this.currentUserSignal.set(user),
				error: () => this.logout(),
			});
		}
	}
}
