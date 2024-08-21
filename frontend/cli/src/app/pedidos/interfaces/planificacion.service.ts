import { Injectable } from "@angular/core";
import { DataPackage } from "../../data-package";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Planificacion } from "./planificacion";
import { PedidoFabricacion } from "./pedidoFabricacion";

@Injectable({
    providedIn: "root",
})
export class PlanificacionService {
    private planificacionUrl = "rest/planificaciones";
    private planificacionProcessUrl = "rest/planificador";

    constructor(private http: HttpClient) {}

    search(term: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(
            `${this.planificacionUrl}/search/${term}`
        );
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.planificacionUrl}/id/${id}`);
    }

    save(planificacion: Planificacion): Observable<DataPackage> {
        return planificacion.id
            ? this.http.put<DataPackage>(this.planificacionUrl, planificacion)
            : this.http.post<DataPackage>(this.planificacionUrl, planificacion);
    }

    remove(id: number): Observable<DataPackage> {
        return this.http.delete<DataPackage>(`${this.planificacionUrl}/${id}`);
    }

    byPage(page: number, size: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(
            `${this.planificacionUrl}/page?page=${page - 1}&size=${size}`
        );
    }

    planificarTodoTardia(fecha: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(
            `${this.planificacionProcessUrl}/pedidos/planificarTodoTardia/${fecha}`
        );
    }
}
