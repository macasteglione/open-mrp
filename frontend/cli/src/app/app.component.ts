import { Component } from "@angular/core";
import { RouterLink, RouterOutlet } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { CommonModule } from "@angular/common";

@Component({
    selector: "app-root",
    standalone: true,
    imports: [RouterOutlet, HomeComponent, RouterLink, CommonModule],
    templateUrl: "app.component.html",
    styleUrl: "app.component.css",
})
export class AppComponent {
    hamMenuActive = false;
    offScreenMenuActive = false;
    loading: boolean = false;

    toggleMenu() {
        this.hamMenuActive = !this.hamMenuActive;
        this.offScreenMenuActive = !this.offScreenMenuActive;
    }

    setLoading(isLoading: boolean) {
        this.loading = isLoading;
    }
}
