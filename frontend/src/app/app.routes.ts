import { Routes } from "@angular/router";

export const routes: Routes = [
	{
		path: "",
		loadComponent: () =>
			import("./composants/post-list/post-list.component").then((m) => m.PostListComponent),
	},
	{
		path: "login",
		loadComponent: () => import("./composants/login/login.component").then((m) => m.LoginComponent),
	},
	{
		path: "signup",
		loadComponent: () =>
			import("./composants/signup/signup.component").then((m) => m.SignupComponent),
	},
	{
		path: "create-post",
		loadComponent: () =>
			import("./composants/create-post/create-post.component").then((m) => m.CreatePostComponent),
	},
	{
		path: "*",
		redirectTo: "",
	},
];
