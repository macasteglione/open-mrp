import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DataPackage } from '../../data-package';
import { Taller } from './taller';

@Injectable({
  providedIn: 'root'
})
export class TalleresService {
  private talleresUrl = "rest/talleres";

  constructor(private http: HttpClient) { }

  search(term: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.talleresUrl}/search/${term}`);
  }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.talleresUrl}/id/${id}`);
  }

  save(taller: Taller): Observable<DataPackage> {
    return taller.id
      ? this.http.put<DataPackage>(this.talleresUrl, taller)
      : this.http.post<DataPackage>(this.talleresUrl, taller);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.talleresUrl}/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(
      `${this.talleresUrl}/page?page=${page - 1}&size=${size}`
    );
  }
}
