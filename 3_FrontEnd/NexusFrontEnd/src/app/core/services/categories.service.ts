
import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Category } from '../models/category.model';
import { API_RESOURCES } from '../constants/api-endpoints';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiBaseUrl}${environment.apiPrefix}${API_RESOURCES.categorias}`;

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.API_URL);
  }

  getCategoryById(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.API_URL}/${id}`);
  }

  createCategory(category: { nombre: string, descripcion: string }): Observable<Category> {
    return this.http.post<Category>(this.API_URL, category);
  }

  updateCategory(id: number, category: { nombre: string, descripcion: string }): Observable<any> {
    return this.http.put(`${this.API_URL}/${id}`, category);
  }

  deleteCategory(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`);
  }

}
