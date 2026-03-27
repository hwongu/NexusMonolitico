import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Revenue, RevenueCreatePayload, RevenueDetail } from '../models/revenue.model';
import { API_RESOURCES, API_SUBROUTES } from '../constants/api-endpoints';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RevenuesService {

  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiBaseUrl}${environment.apiPrefix}${API_RESOURCES.ingresos}`;

  getRevenues(): Observable<Revenue[]> {
    return this.http.get<Revenue[]>(this.API_URL);
  }

  getRevenueDetails(id: number): Observable<RevenueDetail[]> {
    return this.http.get<RevenueDetail[]>(`${this.API_URL}/${id}${API_SUBROUTES.detalles}`);
  }

  createRevenue(payload: RevenueCreatePayload): Observable<any> {
    return this.http.post(this.API_URL, payload);
  }

  updateRevenueStatus(id: number, payload: { estado: string }): Observable<any> {
    return this.http.put(`${this.API_URL}/${id}${API_SUBROUTES.estado}`, payload);
  }

  deleteRevenue(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`);
  }

}
