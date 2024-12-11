import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {T} from '@angular/cdk/keycodes';

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  private baseUrl = 'http://localhost:8080'; // Replace with your backend URL

  constructor(private http: HttpClient) {}

  // Fetch all items from the specified endpoint, with response as text
  get<T>(endpoint: string, options: { responseType: "json" }): Observable<T> {
    // @ts-ignore
    return this.http.get<T>(`${this.baseUrl}/${endpoint}`, options);
  }


  getDashboardData<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}/${endpoint}`);
  }

  // Fetch all items from the specified endpoint
  getAll<T>(endpoint: string): Observable<T[]> {
    return this.http.get<T[]>(`${this.baseUrl}/${endpoint}`);
  }

  // Fetch a single item by its ID from the specified endpoint
  getById<T>(endpoint: string, id: number): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}/${endpoint}/${id}`);
  }

  // Create a new item at the specified endpoint
  create<T>(endpoint: string, payload: T): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}/${endpoint}`, payload);
  }

  // Update an existing item (does not include ID in the URL, matching backend)
  update<T>(endpoint: string, payload: T): Observable<T> {
    return this.http.put<T>(`${this.baseUrl}/${endpoint}`, payload);
  }

  // Delete an item by its ID from the specified endpoint
  delete(endpoint: string, id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${endpoint}/${id}`);
  }
}
