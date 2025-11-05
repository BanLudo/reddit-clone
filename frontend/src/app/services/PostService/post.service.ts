import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { Post, PostPage, PostRequest } from "../../models/post.model";
import { Observable, tap } from "rxjs";

@Injectable({
	providedIn: "root",
})
export class PostService {
	private http = inject(HttpClient);
	private readonly API_URL = "http://localhost:8080/api/posts";

	//états des posts
	private allPostsSignal = signal<Post[]>([]);
	private isLoadingSignal = signal<boolean>(false);
	private currentPageSignal = signal<number>(0);
	private totalPagesSignal = signal<number>(0);

	// computed signal
	public allPosts = this.allPostsSignal.asReadonly();
	public isLoading = this.isLoadingSignal.asReadonly();
	public currentPage = this.currentPageSignal.asReadonly();
	public totalPage = this.totalPagesSignal.asReadonly();

	getAllPosts(page: number = 0, size: number = 10, sort: string = "new"): Observable<PostPage> {
		this.isLoadingSignal.set(true);

		const params = new HttpParams()
			.set("page", page.toString())
			.set("size", size.toString())
			.set("sort", sort);

		return this.http.get<PostPage>(this.API_URL, { params }).pipe(
			tap((response) => {
				if (page === 0) {
					this.allPostsSignal.set(response.content);
				} else {
					this.allPostsSignal.update((posts) => [...posts, ...response.content]);
				}
				this.currentPageSignal.set(response.number);
				this.totalPagesSignal.set(response.totalPages);
				this.isLoadingSignal.set(false);
			})
		);
	}

	getPostById(id: number): Observable<Post> {
		return this.http.get<Post>(`${this.API_URL}/${id}`);
	}

	createPost(postRequest: PostRequest): Observable<Post> {
		return this.http.post<Post>(this.API_URL, postRequest).pipe(
			tap((newPost) => {
				this.allPostsSignal.update((posts) => [newPost, ...posts]);
			})
		);
	}

	updatePost(id: number, postRequest: PostRequest): Observable<Post> {
		return this.http.put<Post>(`${this.API_URL}/${id}`, postRequest).pipe(
			tap((updatedPost) => {
				this.allPostsSignal.update((posts) =>
					posts.map((post) => (post.id === id ? updatedPost : post))
				);
			})
		);
	}

	deletePost(id: number): Observable<void> {
		return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(
			tap(() => {
				this.allPostsSignal.update((posts) => posts.filter((p) => p.id !== id));
			})
		);
	}

	refreshPosts(): void {
		this.getAllPosts(0, 10).subscribe();
	}

	getPostsByUsersId(userId: number, page: number = 0, size: number = 10): Observable<PostPage> {
		let params = new HttpParams().set("page", page.toString()).set("size", size.toString());

		return this.http.get<PostPage>(`${this.API_URL}/user/${userId}`, { params });
	}
}
