import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";
import { catchError, throwError } from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
	const authService = inject(AuthServiceService);
	const token = authService.getToken();

	if (token) {
		const authReq = req.clone({
			headers: req.headers.set("Authorization", `Bearer ${token}`),
		});
		return next(authReq).pipe(
			catchError((error: HttpErrorResponse) => {
				if (error.status === 401) {
					console.error("Token invalid ou expiré, déconnexion...");
					authService.logout();
				}
				return throwError(() => error);
			})
		);
	}

	return next(req);
};
