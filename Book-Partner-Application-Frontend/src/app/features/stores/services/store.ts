import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiResponse } from '../../../models/api-response.model';
import { pickBody, toQueryParams } from '../../../core/api/request.utils';

@Injectable({
  providedIn: 'root',
})
export class Store {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1';

  execute(endpointId: string, values: Record<string, unknown>): Observable<unknown> {
    switch (endpointId) {
      case 'listStores':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/stores`, {
          params: toQueryParams(values, ['city', 'state', 'page', 'size', 'sortBy', 'direction']),
        });
      case 'getStoreById':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/stores/${values['storeId']}`, {
          observe: 'response',
        });
      case 'createStore':
        return this.http.post<ApiResponse<unknown>>(`${this.baseUrl}/stores`, pickBody(values, [
          'storId', 'storName', 'storAddress', 'city', 'state', 'zip',
        ]));
      case 'updateStore':
        return this.http.put<ApiResponse<unknown>>(`${this.baseUrl}/stores/${values['storeId']}`, pickBody(values, [
          'storName', 'storAddress', 'city', 'state', 'zip',
        ]));
      case 'deleteStore':
        return this.http.delete<ApiResponse<unknown>>(`${this.baseUrl}/stores/${values['storeId']}`);
      case 'storeTransactions':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/stores/${values['storeId']}/transactions`);
      case 'storeDiscounts':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/stores/${values['storeId']}/discounts`);
      case 'listDiscounts':
        return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/discounts`);
      default:
        throw new Error(`Unknown store endpoint: ${endpointId}`);
    }
  }
}
