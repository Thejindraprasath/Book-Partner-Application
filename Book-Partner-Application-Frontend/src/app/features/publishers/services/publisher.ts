import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { pickBody, toQueryParams } from '../../../core/api/request.utils';
import { ApiResponse } from '../../../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class Publisher {
  private readonly http = inject(HttpClient);

  // All publisher requests start with this API path.
  private readonly baseUrl = '/api/publishers';

  // The shared endpoint runner calls this method to dispatch one publisher action.
  execute(endpointId: string, values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    switch (endpointId) {
      case 'listPublishers':
        return this.getAllPublishers(values);
      case 'getPublisherById':
        return this.getPublisherById(values);
      case 'createPublisher':
        return this.createPublisher(values);
      case 'updatePublisher':
        return this.updatePublisher(values);
      case 'deletePublisher':
        return this.deletePublisher(values);
      case 'publisherEmployees':
        return this.getPublisherEmployees(values);
      case 'publisherTitles':
        return this.getPublisherTitles(values);
      default:
        throw new Error(`Unknown publisher endpoint: ${endpointId}`);
    }
  }

  private getAllPublishers(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(this.baseUrl, {
      params: toQueryParams(values, ['city', 'state', 'country', 'page', 'size', 'sort']),
    });
  }

  private getPublisherById(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const publisherId = values['pubId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${publisherId}`);
  }

  private createPublisher(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const requestBody = pickBody(values, ['pubId', 'pubName', 'city', 'state', 'country']);
    return this.http.post<ApiResponse<unknown>>(this.baseUrl, requestBody);
  }

  private updatePublisher(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const publisherId = values['pubId'];
    const requestBody = pickBody(values, ['pubName', 'city', 'state', 'country']);

    return this.http.put<ApiResponse<unknown>>(`${this.baseUrl}/${publisherId}`, requestBody);
  }

  private deletePublisher(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const publisherId = values['pubId'];
    return this.http.delete<ApiResponse<unknown>>(`${this.baseUrl}/${publisherId}`);
  }

  private getPublisherEmployees(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const publisherId = values['pubId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${publisherId}/employees`);
  }

  private getPublisherTitles(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const publisherId = values['pubId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${publisherId}/titles`);
  }
}
