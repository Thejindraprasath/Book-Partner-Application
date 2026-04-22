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
  private readonly baseUrl = '/api/v1/transactions';

  execute(endpointId: string, values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    switch (endpointId) {
      case 'listSales':
        return this.http.get<ApiResponse<unknown>>(this.baseUrl, {
          params: toQueryParams(values, ['page', 'size', 'sortBy', 'direction']),
        });
      case 'getSaleById':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${values['ordNum']}`, {
          params: toQueryParams(values, ['storId', 'titleId']),
        });
      case 'createSale':
        return this.http.post<ApiResponse<unknown>>(this.baseUrl, pickBody(values, [
          'storId', 'ordNum', 'ordDate', 'qty', 'payterms', 'titleId',
        ]));
      case 'salesByBranch':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/branch/${values['storId']}`);
      case 'salesByProduct':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/product/${values['titleId']}`);
      case 'salesByDateRange':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/date-range`, {
          params: toQueryParams(values, ['from', 'to']),
        });
      default:
        throw new Error(`Unknown sales endpoint: ${endpointId}`);
    }
  }
}
