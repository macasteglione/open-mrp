import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { DataPackage } from "../../data-package";
import { PedidoFabricacion } from "./pedidoFabricacion";

@Injectable({
    providedIn: "root",
})
export class PedidosService {
    private pedidosUrl = "rest/pedidos";

    constructor(private http: HttpClient) {}

    buscarPedido(
        term: string,
        page: number,
        size: number
    ): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.pedidosUrl}/search`, {
            params: {
                term: term,
                page: (page - 1).toString(),
                size: size.toString(),
            },
        });
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.pedidosUrl}/id/${id}`);
    }

    findAll(): Observable<DataPackage> {
        return this.http.get<DataPackage>(this.pedidosUrl);
    }

    save(pedido: PedidoFabricacion): Observable<DataPackage> {
        return pedido.id
            ? this.http.put<DataPackage>(this.pedidosUrl, pedido)
            : this.http.post<DataPackage>(this.pedidosUrl, pedido);
    }

    remove(id: number): Observable<DataPackage> {
        return this.http.delete<DataPackage>(`${this.pedidosUrl}/${id}`);
    }

    byPage(page: number, size: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(
            `${this.pedidosUrl}/page?page=${page - 1}&size=${size}`
        );
    }
}
