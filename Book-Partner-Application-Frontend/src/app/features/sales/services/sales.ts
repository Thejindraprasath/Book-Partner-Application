import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { pickBody, toQueryParams } from '../../../core/api/request.utils';
import { ApiResponse } from '../../../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class Sales {
  private readonly http = inject(HttpClient);

  // Every sales API call starts with this same base path.
  private readonly baseUrl = '/api/v1/transactions';

  // This method is called by the shared endpoint runner.
  // We keep the switch, but each case now goes to a small helper method.
  execute(endpointId: string, values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    switch (endpointId) {
      case 'listSales':
        return this.getAllSales(values);
      case 'getSaleById':
        return this.getSaleById(values);
      case 'createSale':
        return this.createSale(values);
      case 'salesByBranch':
        return this.getSalesByBranch(values);
      case 'salesByProduct':
        return this.getSalesByProduct(values);
      case 'salesByDateRange':
        return this.getSalesByDateRange(values);
      default:
        throw new Error(`Unknown sales endpoint: ${endpointId}`);
    }
  }

  private getAllSales(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(this.baseUrl, {
      params: toQueryParams(values, ['page', 'size', 'sortBy', 'direction']),
    });
  }

  private getSaleById(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const orderNumber = values['ordNum'];

    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${orderNumber}`, {
      params: toQueryParams(values, ['storId', 'titleId']),
    });
  }

  private createSale(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const requestBody = pickBody(values, [
      'storId',
      'ordNum',
      'ordDate',
      'qty',
      'payterms',
      'titleId',
    ]);

    return this.http.post<ApiResponse<unknown>>(this.baseUrl, requestBody);
  }

  private getSalesByBranch(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const storeId = values['storId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/branch/${storeId}`);
  }

  private getSalesByProduct(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const titleId = values['titleId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/product/${titleId}`);
  }

  private getSalesByDateRange(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/date-range`, {
      params: toQueryParams(values, ['from', 'to']),
    });
  }
}
