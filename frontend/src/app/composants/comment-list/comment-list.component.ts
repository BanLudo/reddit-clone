import { CommonModule } from "@angular/common";
import { Component, Input } from "@angular/core";
import { Comment } from "../../models/comment.model";
import { CommentItemComponent } from "../comment-item/comment-item.component";

@Component({
	selector: "app-comment-list",
	standalone: true,
	imports: [CommonModule, CommentItemComponent],
	templateUrl: "./comment-list.component.html",
	styleUrl: "./comment-list.component.scss",
})
export class CommentListComponent {
	@Input() comments: Comment[] = [];
	@Input() canDo = false;
	@Input() postId!: number;
}
