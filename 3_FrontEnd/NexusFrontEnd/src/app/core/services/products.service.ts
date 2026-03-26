import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Product } from '../models/product.model';
import { API_RESOURCES } from '../constants/api-endpoints';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {

  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiBaseUrl}${environment.apiPrefix}${API_RESOURCES.productos}`;

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.API_URL);
  }

  getProductById(id: number): Observable<Product> {
    return this.getProducts().pipe(
      map(products => {
        const product = products.find(item => item.idProducto === id);

        if (!product) {
          throw new Error(`Product with id ${id} not found`);
        }

        return product;
      })
    );
  }

  createProduct(product: { idCategoria: number, nombre: string, precio: number, stock: number }): Observable<Product> {
    return this.http.post<Product>(this.API_URL, product);
  }

  updateProduct(id: number, product: { idCategoria: number, nombre: string, precio: number, stock: number }): Observable<any> {
    return this.http.put(`${this.API_URL}/${id}`, product);
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`);
  }

}
