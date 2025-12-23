import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import {map, Observable, throwError} from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private authUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

  login(identifier: string, password: string) {
    const body = new FormData();
    if (identifier.includes('@')) {
      body.set('email', identifier);
    } else {
      body.set('login', identifier);
    }
    body.set('password', password);

    return this.http.post('http://localhost:8080/api/auth/login', body, { withCredentials: true });
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.authUrl}/logout`, null, {
      withCredentials: true
    });
  }

  register(login: string, email: string, password: string, passwordHint: string): Observable<any> {
    const body = new FormData();
    body.set('login', login);
    body.set('email', email);
    body.set('password', password);
    body.set('passwordHint', passwordHint);
    return this.http.post(`${this.authUrl}/register`, body);
  }

  refreshToken(): Observable<any> {
    return this.http.post(`${this.authUrl}/refresh`, {});
  }

  private secureRequest<T>(request: () => Observable<T>): Observable<T> {
    return request().pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          return this.refreshToken().pipe(
            switchMap(() => request()),
            catchError(() => {
              this.router.navigate(['/login']);
              return throwError(() => new Error('Unauthorized'));
            })
          );
        }
        return throwError(() => error);
      })
    );
  }

  checkAuth() {
    return this.http.get<any>(`${this.authUrl}/check-auth`, { withCredentials: true });
  }
}
