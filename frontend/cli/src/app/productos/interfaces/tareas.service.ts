import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DataPackage } from '../../data-package';
import { Tarea } from './tarea';

@Injectable({
    providedIn: 'root'
})
export class TareasService {
    private tareasUrl = "rest/tareas";

    constructor(private http: HttpClient) { }

    search(term: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.tareasUrl}/search/${term}`);
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.tareasUrl}/id/${id}`);
    }

    save(tarea: Tarea): Observable<DataPackage> {
        return tarea.id
            ? this.http.put<DataPackage>(this.tareasUrl, tarea)
            : this.http.post<DataPackage>(this.tareasUrl, tarea);
    }

    remove(id: number): Observable<DataPackage> {
        return this.http.delete<DataPackage>(`${this.tareasUrl}/${id}`);
    }

    byPage(page: number, size: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(
            `${this.tareasUrl}/page?page=${page - 1}&size=${size}`
        );
    }
}
