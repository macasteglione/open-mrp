import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";
import { PaginationComponent } from "../../pagination/pagination.component";
import { ResultsPage } from "../../results-page";
import { PedidosService } from "../interfaces/pedidos.service";
import { ModalService } from "../../modal/modal.service";
import { PlanificacionService } from "../interfaces/planificacion.service";
import { PedidoFabricacion } from "../interfaces/pedidoFabricacion";
import { FormsModule } from "@angular/forms";
import { AppComponent } from "../../app.component";

@Component({
    selector: "app-pedidos",
    standalone: true,
    imports: [CommonModule, RouterModule, PaginationComponent, FormsModule],
    templateUrl: "pedidos.component.html",
    styleUrls: ["pedidos.component.css"],
})
export class PedidosComponent {
    resultsPage: ResultsPage = <ResultsPage>{};
    pedidos!: PedidoFabricacion[];
    currentPage: number = 1;
    pageSize: number = 10;
    fecha!: string;
    searchTerm: string = "";

    constructor(
        private pedidosService: PedidosService,
        private modalService: ModalService,
        private planificacionService: PlanificacionService,
        private appComponent: AppComponent,
    ) {}

    getPedidos(): void {
        this.pedidosService
            .byPage(this.currentPage, this.pageSize)
            .subscribe((dataPackage) => {
                this.resultsPage = <ResultsPage>dataPackage.data;
            });
    }

    ngOnInit(): void {
        this.getPedidos();
    }

    remove(id: number): void {
        let that = this;
        this.modalService
            .confirm(
                "Eliminar pedido.",
                "¿Está seguro de que quiere eliminar el pedido?",
                "Si elimina el pedido, no lo podrá utilizar luego."
            )
            .then(function () {
                that.pedidosService
                    .remove(id)
                    .subscribe(() => that.getPedidos());
            });
    }

    onPageChangeRequested(page: number): void {
        this.currentPage = page;
        this.searchTerm == "" ? this.getPedidos() : this.buscarPedido();
    }

    formatDate(date: Date): string {
        if (date) return new Date(date).toLocaleDateString("es-ES");
        return "";
    }

    buscarPedido() {
        this.pedidosService
            .buscarPedido(this.searchTerm, this.currentPage, this.pageSize)
            .subscribe((dataPackage) => {
                this.resultsPage = <ResultsPage>dataPackage.data;
            });
    }

    onSearch() {
        this.currentPage = 1;
        this.buscarPedido();
    }

    planificarTodoTardia() {
        this.appComponent.setLoading(true);
        this.planificacionService
            .planificarTodoTardia(this.fecha)
            .subscribe(() => {
                window.location.reload();
                this.appComponent.setLoading(false);
            });
    }

    highlight(text: string, search: string): string {
        if (!search) return text;
        const regex = new RegExp(`(${search})`, "gi");
        return text.replace(regex, "<mark>$1</mark>");
    }
}
