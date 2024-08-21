import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TipoEquiposService } from '../interfaces/tipoEquipos.service';
import { ResultsPage } from '../../../results-page';
import { ModalService } from '../../../modal/modal.service';
import { PaginationComponent } from '../../../pagination/pagination.component';

@Component({
  selector: 'app-tipo-equipos',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: "tipoEquipos.component.html",
  styleUrl: "tipoEquipos.component.css"
})
export class TipoEquiposComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  pageSize: number = 10;

  constructor(
    private tipoEquiposService: TipoEquiposService,
    private modalService: ModalService
  ) { }

  getClientes(): void {
    this.tipoEquiposService
      .byPage(this.currentPage, this.pageSize)
      .subscribe(
        (dataPackage) => {
          this.resultsPage = <ResultsPage>dataPackage.data;
        }
      );
  }

  ngOnInit(): void {
    this.getClientes();
  }

  remove(id: number): void {
    let that = this;
    this.modalService
      .confirm(
        "Eliminar cliente.",
        "¿Esta seguro de que quiere eliminar el cliente?",
        "Si elimina el cliente, no lo podra utilizar luego."
      )
      .then(function () {
        that.tipoEquiposService
          .remove(id)
          .subscribe((dataPackage) => that.getClientes());
      });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getClientes();
  }
}
