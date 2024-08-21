import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DataPackage } from '../../data-package';
import { Cliente } from './cliente';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private clienteUrl = "rest/clientes";

  constructor(private http: HttpClient) { }

  search(term: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.clienteUrl}/search/${term}`);
  }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.clienteUrl}/id/${id}`);
  }

  save(cliente: Cliente): Observable<DataPackage> {
    return cliente.id
      ? this.http.put<DataPackage>(this.clienteUrl, cliente)
      : this.http.post<DataPackage>(this.clienteUrl, cliente);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.clienteUrl}/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(
      `${this.clienteUrl}/page?page=${page - 1}&size=${size}`
    );
  }
}
