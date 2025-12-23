import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Result } from '../models/result.model';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'http://localhost:8080/api/index';

  constructor(private http: HttpClient) {}

  submit(x: number, y: number, r: number): Observable<void> {
    let body = new HttpParams()
    .set('xValue', x.toString())
    .set('yValue', y.toString())
    .set('rValue', r.toString());
    return this.http.post<void>(`${this.baseUrl}/submit`, body.toString(), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      withCredentials: true,
    });
  }

  getResults(): Observable<Result[]> {
    return this.http.get<Result[]>(`${this.baseUrl}/data`, {
      withCredentials: true,
    });
  }

  clearResults(): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/clear`, {
      withCredentials: true,
    });
  }
}
