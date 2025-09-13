import { HttpClient } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { Post } from "../../models/post.model";

@Injectable({
	providedIn: "root",
})
export class PostService {
	private http = inject(HttpClient);
	private readonly API_URL = "http://localhost:8080/api";

	//états des post
	private currentPostSignal = signal<Post | null>(null);
	private isLoadingSignal = signal<boolean>(false);
	private allPostsSignal = signal<Post[]>([]);

	//
	public currentPost = this.currentPostSignal.asReadonly();
	public isLoading = this.isLoadingSignal.asReadonly();
	public allPosts = this.allPostsSignal.asReadonly();
}
