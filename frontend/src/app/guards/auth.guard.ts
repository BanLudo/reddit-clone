import { CanActivateFn, Router } from "@angular/router";
import { AuthServiceService } from "../services/AuthService/auth-service.service";
import { inject } from "@angular/core";

export const authGuard: CanActivateFn = (route, state) => {
	const authService = inject(AuthServiceService);
	const router = inject(Router);

	if (authService.isLoggedIn()) return true;

	router.navigate(["/login"]);
	return false;
};
