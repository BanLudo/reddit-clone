import { CanActivateFn, Router } from "@angular/router";
import { AuthServiceService } from "../services/AuthService/auth-service.service";
import { inject } from "@angular/core";

export const authGuard: CanActivateFn = (route, state) => {
	const authService = inject(AuthServiceService);
	const router = inject(Router);

	if (authService.isLoggedIn() && authService.isTokenValid()) return true;

	//token présent mais invalid/expiré
	if (authService.getToken()) {
		authService.logout();
	}

	router.navigate(["/login"], { queryParams: { returnurl: state.url } });

	return false;
};
