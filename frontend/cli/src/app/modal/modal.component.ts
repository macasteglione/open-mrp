import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: "app-modal",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "modal.component.html",
  styles: ``,
})
export class ModalComponent {
  constructor(public modal: NgbActiveModal) { }

  title = "";
  message = "";
  description = "";
}