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
		path: "post/:id",
		loadComponent: () =>
			import("./composants/post-detail/post-detail.component").then((m) => m.PostDetailComponent),
	},
	{
		path: "edit-post/:id",
		loadComponent: () =>
			import("./composants/edit-post/edit-post.component").then((m) => m.EditPostComponent),
	},
	{
		path: "create-post",
		loadComponent: () =>
			import("./composants/create-post/create-post.component").then((m) => m.CreatePostComponent),
	},
	{
		path: "profile",
		loadComponent: () =>
			import("./composants/profile/profile.component").then((m) => m.ProfileComponent),
	},
	{
		path: "**",
		redirectTo: "",
	},
];
