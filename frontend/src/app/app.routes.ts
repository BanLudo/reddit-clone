import { Routes } from "@angular/router";

export const routes: Routes = [
	{
		path: "auth/login",
		loadComponent: () => import("./composants/login/login.component").then((m) => m.LoginComponent),
	},
	{
		path: "auth/signup",
		loadComponent: () =>
			import("./composants/signup/signup.component").then((m) => m.SignupComponent),
	},
	{
		path: "*",
		redirectTo: "",
	},
];
