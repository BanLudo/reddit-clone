import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { VoteRequest, VoteType } from "../../models/vote.model";
import { Observable } from "rxjs";

@Injectable({
	providedIn: "root",
})
export class VoteService {
	private http = inject(HttpClient);
	private readonly API_URL = "http://localhost:8080/api";

	votePost(postId: number, voteRequest: VoteRequest): Observable<void> {
		return this.http.post<void>(`${this.API_URL}/posts/${postId}/vote`, voteRequest);
	}

	voteComment(commentId: number, voteRequest: VoteRequest): Observable<void> {
		return this.http.post<void>(`${this.API_URL}/comments/${commentId}/vote`, voteRequest);
	}
}
