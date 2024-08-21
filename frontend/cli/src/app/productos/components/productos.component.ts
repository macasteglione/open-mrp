import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PaginationComponent } from '../../pagination/pagination.component';
import { ResultsPage } from '../../results-page';
import { ProductosService } from '../interfaces/productos.service';
import { ModalService } from '../../modal/modal.service';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: "productos.component.html",
  styleUrl: "productos.component.css"
})
export class ProductosComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  pageSize: number = 10;

  constructor(
    private productoService: ProductosService,
    private modalService: ModalService
  ) { }

  getClientes(): void {
    this.productoService
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
        "Eliminar producto.",
        "¿Esta seguro de que quiere eliminar el producto?",
        "Si elimina el producto, no lo podra utilizar luego."
      )
      .then(function () {
        that.productoService
          .remove(id)
          .subscribe((dataPackage) => that.getClientes());
      });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getClientes();
  }
}
