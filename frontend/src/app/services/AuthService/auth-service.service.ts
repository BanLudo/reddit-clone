import { HttpClient } from "@angular/common/http";
import { computed, inject, Injectable, signal } from "@angular/core";
import { User } from "../../models/user.model";
import { ApiResponse, AuthRequest, AuthResponse, RegisterRequest } from "../../models/auth.model";
import { catchError, Observable, tap } from "rxjs";
import { Router } from "@angular/router";

@Injectable({
	providedIn: "root",
})
export class AuthServiceService {
	private http = inject(HttpClient);
	private router = inject(Router);

	private readonly API_URL = "http://localhost:8080/api";
	private readonly TOKEN_KEY = "auth-token";

	//gestion etats
	private currentUserSignal = signal<User | null>(null);
	private isLoadingSignal = signal<boolean>(false);

	//computed signal
	public currentUser = this.currentUserSignal.asReadonly();
	public isLoading = this.isLoadingSignal.asReadonly();
	public isAuthenticated = computed(() => this.currentUserSignal() !== null);

	constructor() {
		this.loadUserFromStorage();
	}

	register(userData: RegisterRequest): Observable<ApiResponse> {
		this.isLoadingSignal.set(true);

		return this.http.post<ApiResponse>(`${this.API_URL}/auth/signup`, userData);
	}

	login(credential: AuthRequest): Observable<AuthResponse> {
		this.isLoadingSignal.set(true);

		return this.http.post<AuthResponse>(`${this.API_URL}/auth/login`, credential).pipe(
			tap((response) => {
				localStorage.setItem(this.TOKEN_KEY, response.token);
				this.currentUserSignal.set(response.user);
				this.isLoadingSignal.set(false);
			}),
			catchError((error) => {
				this.isLoadingSignal.set(false);
				throw error;
			})
		);
	}

	getCurrentUser(): Observable<User> {
		return this.http.get<User>(`${this.API_URL}/user/me`);
	}

	logout(): void {
		localStorage.removeItem(this.TOKEN_KEY);
		this.currentUserSignal.set(null);
		this.isLoadingSignal.set(false);
		this.router.navigate(["/"]);
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
