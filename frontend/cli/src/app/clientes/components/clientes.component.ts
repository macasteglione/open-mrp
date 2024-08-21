import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ClienteService } from '../interfaces/clientes.service';
import { PaginationComponent } from '../../pagination/pagination.component';
import { ResultsPage } from '../../results-page';
import { ModalService } from '../../modal/modal.service';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: "clientes.component.html",
  styleUrl: "clientes.component.css"
})
export class ClientesComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  pageSize: number = 10;

  constructor(
    private clienteService: ClienteService,
    private modalService: ModalService
  ) { }

  getClientes(): void {
    this.clienteService
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
        that.clienteService
          .remove(id)
          .subscribe((dataPackage) => that.getClientes());
      });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getClientes();
  }
}
