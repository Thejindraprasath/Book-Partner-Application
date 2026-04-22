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
  private readonly baseUrl = '/api/titles';

  execute(endpointId: string, values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    switch (endpointId) {
      case 'listBooks':
        return this.http.get<ApiResponse<unknown>>(this.baseUrl, {
          params: toQueryParams(values, ['type', 'pubId', 'minPrice', 'maxPrice', 'page', 'size', 'sort']),
        });
      case 'getBookById':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${values['id']}`);
      case 'createBook':
        return this.http.post<ApiResponse<unknown>>(this.baseUrl, pickBody(values, [
          'titleId', 'title', 'type', 'pubId', 'price', 'advance', 'royalty', 'ytdSales', 'notes', 'pubdate',
        ]));
      case 'updateBook':
        return this.http.put<ApiResponse<unknown>>(`${this.baseUrl}/${values['id']}`, pickBody(values, [
          'title', 'type', 'pubId', 'price', 'advance', 'royalty', 'ytdSales', 'notes', 'pubdate',
        ]));
      case 'deleteBook':
        return this.http.delete<ApiResponse<unknown>>(`${this.baseUrl}/${values['id']}`);
      default:
        throw new Error(`Unknown book endpoint: ${endpointId}`);
    }
  }
}
