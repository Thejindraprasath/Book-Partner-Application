import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { pickBody, toQueryParams } from '../../../core/api/request.utils';
import { ApiResponse } from '../../../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class Author {
  private readonly http = inject(HttpClient);

  // Every author API call starts with this same base path.
  private readonly baseUrl = '/api/v1/authors';

  // The shared endpoint runner calls this method.
  // Each case goes to a small helper method so the file is easier to read.
  execute(endpointId: string, values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    switch (endpointId) {
      case 'listAuthors':
        return this.getAllAuthors(values);
      case 'getAuthorById':
        return this.getAuthorById(values);
      case 'createAuthor':
        return this.createAuthor(values);
      case 'updateAuthor':
        return this.updateAuthor(values);
      case 'deleteAuthor':
        return this.deleteAuthor(values);
      case 'authorTitles':
        return this.getAuthorTitles(values);
      default:
        throw new Error(`Unknown author endpoint: ${endpointId}`);
    }
  }

  private getAllAuthors(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(this.baseUrl, {
      params: toQueryParams(values, [
        'city',
        'state',
        'contract',
        'page',
        'size',
        'sortBy',
        'direction',
      ]),
    });
  }

  private getAuthorById(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const authorId = values['auId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${authorId}`);
  }

  private createAuthor(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const requestBody = pickBody(values, [
      'auId',
      'auLname',
      'auFname',
      'phone',
      'address',
      'city',
      'state',
      'zip',
      'contract',
    ]);

    return this.http.post<ApiResponse<unknown>>(this.baseUrl, requestBody);
  }

  private updateAuthor(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const authorId = values['auId'];
    const requestBody = pickBody(values, [
      'auLname',
      'auFname',
      'phone',
      'address',
      'city',
      'state',
      'zip',
      'contract',
    ]);

    return this.http.put<ApiResponse<unknown>>(`${this.baseUrl}/${authorId}`, requestBody);
  }

  private deleteAuthor(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const authorId = values['auId'];
    return this.http.delete<ApiResponse<unknown>>(`${this.baseUrl}/${authorId}`);
  }

  private getAuthorTitles(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const authorId = values['auId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${authorId}/titles`);
  }
}
