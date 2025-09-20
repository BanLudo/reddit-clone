import { HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { AuthServiceService } from "../../services/AuthService/auth-service.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
	const authService = inject(AuthServiceService);
	const token = authService.getToken();

	if (token) {
		const authReq = req.clone({
			headers: req.headers.set("Authorization", `Bearer ${token}`),
		});
		return next(authReq);
	}

	return next(req);
};
