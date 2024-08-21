import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DataPackage } from '../../data-package';
import { Observable } from 'rxjs';
import { Producto } from './producto';

@Injectable({
  providedIn: 'root'
})
export class ProductosService {
  private productosUrl = "rest/productos";

  constructor(private http: HttpClient) { }

  search(term: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.productosUrl}/search/${term}`);
  }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.productosUrl}/id/${id}`);
  }

  save(producto: Producto): Observable<DataPackage> {
    return producto.id
      ? this.http.put<DataPackage>(this.productosUrl, producto)
      : this.http.post<DataPackage>(this.productosUrl, producto);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.productosUrl}/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(
      `${this.productosUrl}/page?page=${page - 1}&size=${size}`
    );
  }
}
