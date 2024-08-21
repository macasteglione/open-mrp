import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Equipo } from './equipo';
import { DataPackage } from '../../data-package';

@Injectable({
    providedIn: 'root'
})
export class EquiposService {
    private equiposUrl = "rest/equipos";

    constructor(private http: HttpClient) { }

    search(term: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.equiposUrl}/search/${term}`);
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.equiposUrl}/id/${id}`);
    }

    save(equipo: Equipo): Observable<DataPackage> {
        return equipo.id
            ? this.http.put<DataPackage>(this.equiposUrl, equipo)
            : this.http.post<DataPackage>(this.equiposUrl, equipo);
    }

    remove(id: number): Observable<DataPackage> {
        return this.http.delete<DataPackage>(`${this.equiposUrl}/${id}`);
    }

    byPage(page: number, size: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(
            `${this.equiposUrl}/page?page=${page - 1}&size=${size}`
        );
    }
}
