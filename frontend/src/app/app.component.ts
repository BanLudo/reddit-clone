import { Component, inject } from "@angular/core";
import { RouterModule, RouterOutlet } from "@angular/router";
import { MatToolbarModule } from "@angular/material/toolbar";
import { CommonModule } from "@angular/common";
import { AuthServiceService } from "./services/AuthService/auth-service.service";
import { MatIconModule } from "@angular/material/icon";
import { MatMenuModule } from "@angular/material/menu";
import { MatButtonModule } from "@angular/material/button";

@Component({
	selector: "app-root",
	standalone: true,
	imports: [
		RouterOutlet,
		MatToolbarModule,
		CommonModule,
		MatIconModule,
		MatMenuModule,
		MatButtonModule,
		RouterModule,
	],
	templateUrl: "./app.component.html",
	styleUrl: "./app.component.scss",
})
export class AppComponent {
	authService = inject(AuthServiceService);

	logout() {
		this.authService.logout();
	}
}
