import { HttpClient } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { Comment, CommentRequest } from "../../models/comment.model";
import { Observable, tap } from "rxjs";

@Injectable({
	providedIn: "root",
})
export class CommentService {
	private http = inject(HttpClient);
	private readonly API_URL = "http://localhost:8080/api";

	private loadingSignal = signal<boolean>(false);
	commentsSignal = signal<{ [postId: number]: Comment[] }>({}); //obligé de le mettre non-private.. pour refresh les reply

	loading = this.loadingSignal.asReadonly();
	comments = this.commentsSignal.asReadonly();

	createComment(postId: number, commentRequest: CommentRequest): Observable<Comment> {
		return this.http.post<Comment>(`${this.API_URL}/posts/${postId}/comments`, commentRequest).pipe(
			tap((newComment) => {
				this.commentsSignal.update((commentsMap) => ({
					...commentsMap,
					[postId]: [newComment, ...(commentsMap[postId] || [])],
				}));
			})
		);
	}

	deleteComment(id: number, postId: number): Observable<void> {
		return this.http.delete<void>(`${this.API_URL}/comments/${id}`).pipe(
			tap(() => {
				this.commentsSignal.update((commentsMap) => ({
					...commentsMap,
					[postId]: (commentsMap[postId] || []).filter((comment) => comment.id !== id),
				}));
			})
		);
	}

	updateComment(id: number, commentRequest: CommentRequest): Observable<Comment> {
		return this.http.put<Comment>(`${this.API_URL}/comments/${id}`, commentRequest).pipe(
			tap((updatedComment) => {
				this.commentsSignal.update((commentsMap) => {
					const updateMap = { ...commentsMap };

					Object.keys(updateMap).forEach((postId: any) => {
						updateMap[postId] = this.updateCommentInList(updateMap[postId], updatedComment);
					});

					return updateMap;
				});
			})
		);
	}

	getCommentsByPostId(postId: number): Observable<Comment[]> {
		this.loadingSignal.set(true);

		return this.http.get<Comment[]>(`${this.API_URL}/posts/${postId}/comments`).pipe(
			tap((comments) => {
				this.commentsSignal.update((commentsMap) => ({
					...commentsMap,
					[postId]: comments,
				}));
				this.loadingSignal.set(false);
			})
		);
	}

	private updateCommentInList(comments: Comment[], updatedComment: Comment): Comment[] {
		return comments.map((comment) => {
			if (comment.id === updatedComment.id) {
				return { ...comment, ...updatedComment };
			}

			//si le commentaire a des réponse, mettre à jour
			if (comment.replies && comment.replies.length > 0) {
				return { ...comment, replies: this.updateCommentInList(comment.replies, updatedComment) };
			}
			return comment;
		});
	}
} //
