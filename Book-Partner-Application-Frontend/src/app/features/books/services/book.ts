import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { pickBody, toQueryParams } from '../../../core/api/request.utils';
import { ApiResponse } from '../../../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private readonly http = inject(HttpClient);

  // Every book API call starts with this same base path.
  private readonly baseUrl = '/api/titles';

  // The shared endpoint runner calls this method.
  // Each case goes to a small helper method so the file is easier to read.
  execute(endpointId: string, values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    switch (endpointId) {
      case 'listBooks':
        return this.getAllBooks(values);
      case 'getBookById':
        return this.getBookById(values);
      case 'createBook':
        return this.createBook(values);
      case 'updateBook':
        return this.updateBook(values);
      case 'deleteBook':
        return this.deleteBook(values);
      default:
        throw new Error(`Unknown book endpoint: ${endpointId}`);
    }
  }

  private getAllBooks(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(this.baseUrl, {
      params: toQueryParams(values, ['type', 'pubId', 'minPrice', 'maxPrice', 'page', 'size', 'sort']),
    });
  }

  private getBookById(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const bookId = values['id'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${bookId}`);
  }

  private createBook(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const requestBody = pickBody(values, [
      'titleId',
      'title',
      'type',
      'pubId',
      'price',
      'advance',
      'royalty',
      'ytdSales',
      'notes',
      'pubdate',
    ]);

    return this.http.post<ApiResponse<unknown>>(this.baseUrl, requestBody);
  }

  private updateBook(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const bookId = values['id'];
    const requestBody = pickBody(values, [
      'title',
      'type',
      'pubId',
      'price',
      'advance',
      'royalty',
      'ytdSales',
      'notes',
      'pubdate',
    ]);

    return this.http.put<ApiResponse<unknown>>(`${this.baseUrl}/${bookId}`, requestBody);
  }

  private deleteBook(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const bookId = values['id'];
    return this.http.delete<ApiResponse<unknown>>(`${this.baseUrl}/${bookId}`);
  }
}
