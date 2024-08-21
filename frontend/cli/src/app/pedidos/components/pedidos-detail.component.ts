import { CommonModule, Location } from "@angular/common";
import { Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbTypeaheadModule } from "@ng-bootstrap/ng-bootstrap";
import { PedidoFabricacion } from "../interfaces/pedidoFabricacion";
import { ActivatedRoute } from "@angular/router";
import { PedidosService } from "../interfaces/pedidos.service";
import { ClienteService } from "../../clientes/interfaces/clientes.service";
import { ProductosService } from "../../productos/interfaces/productos.service";
import { Cliente } from "../../clientes/interfaces/cliente";
import {
    Observable,
    catchError,
    debounceTime,
    distinctUntilChanged,
    map,
    of,
    switchMap,
    tap,
} from "rxjs";
import { Producto } from "../../productos/interfaces/producto";
import { Estado } from "../interfaces/estado";
import { Planificacion } from "../interfaces/planificacion";

@Component({
    selector: "app-pedidos-detail",
    standalone: true,
    imports: [CommonModule, FormsModule, NgbTypeaheadModule],
    templateUrl: "pedidos-detail.component.html",
    styleUrl: "pedidos-detail.component.css",
})
export class PedidosDetailComponent {
    pedido!: PedidoFabricacion;
    searching: boolean = false;
    searchFailed: boolean = false;
    fechaPedido!: string;
    fechaEntrega!: string;

    constructor(
        private route: ActivatedRoute,
        private pedidoService: PedidosService,
        private clienteService: ClienteService,
        private productoService: ProductosService,
        private location: Location,
    ) {}

    ngOnInit() {
        this.get();
    }

    get() {
        const id = this.route.snapshot.paramMap.get("id");
        if (id === "new") {
            this.pedido = <PedidoFabricacion>{
                producto: <Producto>{},
                cliente: <Cliente>{},
                planificaciones: <Planificacion[]>[],
            };
        } else {
            this.pedidoService.get(parseInt(id!)).subscribe((dataPackage) => {
                this.pedido = <PedidoFabricacion>dataPackage.data;
                this.fechaEntrega = new Date(this.pedido.fechaEntrega)
                    .toISOString()
                    .slice(0, 10);
                this.fechaPedido = new Date(this.pedido.fechaPedido)
                    .toISOString()
                    .slice(0, 10);
            });
        }
    }

    goBack() {
        this.location.back();
    }

    save() {
        this.pedido.fechaPedido = new Date(
            parseInt(this.fechaPedido.slice(0, 4)),
            parseInt(this.fechaPedido.slice(5, 7)) - 1,
            parseInt(this.fechaPedido.slice(8, 10))
        );

        this.pedido.fechaEntrega = new Date(
            parseInt(this.fechaEntrega.slice(0, 4)),
            parseInt(this.fechaEntrega.slice(5, 7)) - 1,
            parseInt(this.fechaEntrega.slice(8, 10))
        );

        this.pedido.estado = Estado.PENDIENTE;

        this.pedidoService.save(this.pedido).subscribe((dataPackage) => {
            this.pedido = <PedidoFabricacion>dataPackage.data;
        });
    }

    searchCliente = (text$: Observable<string>): Observable<any[]> =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => (this.searching = true)),
            switchMap((term) =>
                this.clienteService
                    .search(term)
                    .pipe(
                        map((response) => {
                            let clientes = <Cliente[]>response.data;
                            return clientes;
                        })
                    )
                    .pipe(
                        tap(() => (this.searchFailed = false)),
                        catchError(() => {
                            this.searchFailed = true;
                            return of([]);
                        })
                    )
            ),
            tap(() => (this.searching = false))
        );

    searchProducto = (text$: Observable<string>): Observable<any[]> =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => (this.searching = true)),
            switchMap((term) =>
                this.productoService
                    .search(term)
                    .pipe(
                        map((response) => {
                            let productos = <Producto[]>response.data;
                            return productos;
                        })
                    )
                    .pipe(
                        tap(() => (this.searchFailed = false)),
                        catchError(() => {
                            this.searchFailed = true;
                            return of([]);
                        })
                    )
            ),
            tap(() => (this.searching = false))
        );

    resultFormat(value: any) {
        return value.nombre;
    }

    inputFormat(value: any) {
        return value ? value.nombre : null;
    }

    cuitFormat(cuit: number): string {
        const cuitStr = cuit.toString().padStart(11, "0");
        return `${cuitStr.slice(0, 2)}-${cuitStr.slice(2, 10)}-${cuitStr.slice(
            10
        )}`;
    }
}
