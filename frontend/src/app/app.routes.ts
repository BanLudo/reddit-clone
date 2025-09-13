import { Routes } from "@angular/router";

export const routes: Routes = [
	{
		path: "auth/login",
	},
	{
		path: "auth/signup",
	},
	{
		path: "profile",
	},
	{
		path: "*",
		redirectTo: "",
	},
];
