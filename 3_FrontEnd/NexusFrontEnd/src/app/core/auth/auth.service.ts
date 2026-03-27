
import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { API_RESOURCES, API_SUBROUTES } from '../constants/api-endpoints';
import { environment } from '../../../environments/environment';

/**
 * @author Henry Wong (hwongu@gmail.com)
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private http = inject(HttpClient);
  private router = inject(Router);

  private readonly API_URL = `${environment.apiBaseUrl}${environment.apiPrefix}${API_RESOURCES.usuarios}`;
  private readonly USER_KEY = 'current_user';
  private readonly EXPIRATION_KEY = 'session_expiration';

  login(credentials: any) {
    console.log('Sending credentials:', credentials);
    return this.http.post<any>(`${this.API_URL}${API_SUBROUTES.login}`, credentials).pipe(
      tap(user => {
        this.setSession(user);
      })
    );
  }

  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    if (typeof window !== 'undefined') {
      const expiration = localStorage.getItem(this.EXPIRATION_KEY);
      if (expiration && new Date().getTime() < parseInt(expiration, 10)) {
        return true;
      }
    }
    this.clearSession();
    return false;
  }

  getUser() {
    if (typeof window !== 'undefined') {
      const user = localStorage.getItem(this.USER_KEY);
      return user ? JSON.parse(user) : null;
    }
    return null;
  }

  private setSession(user: any) {
    const expiration = new Date().getTime() + (60 * 1000); // 60 seconds
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    localStorage.setItem(this.EXPIRATION_KEY, expiration.toString());
  }

  private clearSession() {
    if (typeof window !== 'undefined') {
      localStorage.removeItem(this.USER_KEY);
      localStorage.removeItem(this.EXPIRATION_KEY);
    }
  }
}
