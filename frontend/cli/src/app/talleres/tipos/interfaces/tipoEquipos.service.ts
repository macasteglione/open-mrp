import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TipoEquipo } from './tipoEquipo';
import { DataPackage } from '../../../data-package';

@Injectable({
  providedIn: 'root'
})
export class TipoEquiposService {
  private tipoEquiposUrl = "rest/tiposEquipos";

  constructor(private http: HttpClient) { }

  search(term: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.tipoEquiposUrl}/search/${term}`);
  }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.tipoEquiposUrl}/id/${id}`);
  }

  save(tipoEquipo: TipoEquipo): Observable<DataPackage> {
    return tipoEquipo.id
      ? this.http.put<DataPackage>(this.tipoEquiposUrl, tipoEquipo)
      : this.http.post<DataPackage>(this.tipoEquiposUrl, tipoEquipo);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.tipoEquiposUrl}/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(
      `${this.tipoEquiposUrl}/page?page=${page - 1}&size=${size}`
    );
  }
}
