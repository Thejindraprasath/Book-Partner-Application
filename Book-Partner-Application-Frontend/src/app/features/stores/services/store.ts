import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { pickBody, toQueryParams } from '../../../core/api/request.utils';
import { ApiResponse } from '../../../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class Store {
  private readonly http = inject(HttpClient);

  // Store endpoints share the same API version prefix.
  private readonly baseUrl = '/api/v1';

  // The shared endpoint runner calls this method.
  // Each case goes to a small helper method so the file is easier to read.
  execute(endpointId: string, values: Record<string, unknown>): Observable<unknown> {
    switch (endpointId) {
      case 'listStores':
        return this.getAllStores(values);
      case 'getStoreById':
        return this.getStoreById(values);
      case 'createStore':
        return this.createStore(values);
      case 'updateStore':
        return this.updateStore(values);
      case 'deleteStore':
        return this.deleteStore(values);
      case 'storeTransactions':
        return this.getStoreTransactions(values);
      case 'storeDiscounts':
        return this.getStoreDiscounts(values);
      case 'listDiscounts':
        return this.getAllDiscounts();
        case 'createDiscount':
                return this.createDiscount(values);
      case 'getDiscountByType':
        return this.getDiscountByType(values);
      case 'updateDiscount':
        return this.updateDiscount(values);
      default:
        throw new Error(`Unknown store endpoint: ${endpointId}`);
    }
  }

  private getAllStores(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/stores`, {
      params: toQueryParams(values, ['city', 'state', 'page', 'size', 'sortBy', 'direction']),
    });
  }

  private getStoreById(values: Record<string, unknown>): Observable<unknown> {
    const storeId = values['storeId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/stores/${storeId}`, {
      observe: 'response',
    });
  }

  private createStore(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const requestBody = pickBody(values, [
      'storId',
      'storName',
      'storAddress',
      'city',
      'state',
      'zip',
    ]);

    return this.http.post<ApiResponse<unknown>>(`${this.baseUrl}/stores`, requestBody);
  }

  private updateStore(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const storeId = values['storeId'];
    const requestBody = pickBody(values, ['storName', 'storAddress', 'city', 'state', 'zip']);

    return this.http.put<ApiResponse<unknown>>(`${this.baseUrl}/stores/${storeId}`, requestBody);
  }

  private deleteStore(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const storeId = values['storeId'];
    return this.http.delete<ApiResponse<unknown>>(`${this.baseUrl}/stores/${storeId}`);
  }

  private getStoreTransactions(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const storeId = values['storeId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/stores/${storeId}/transactions`);
  }

  private getStoreDiscounts(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const storeId = values['storeId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/stores/${storeId}/discounts`);
  }

  private getAllDiscounts(): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/discounts`);
  }
  private createDiscount(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const requestBody = {
      discounttype: values['discounttype']?? values['discountType'],
      storId: values['storId'],
      lowqty: values['lowqty'],
      highqty: values['highqty'],
      discount: values['discount'],
    };

    return this.http.post<ApiResponse<unknown>>(
      `${this.baseUrl}/discounts`,
      requestBody
    );
  }
  private getDiscountByType(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
      const discountType = values['discountType'];

      return this.http.get<ApiResponse<unknown>>(
        `${this.baseUrl}/discounts/${discountType}`
      );
    }
  private updateDiscount(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
      const discountId = values['discountId'];

      const requestBody = pickBody(values, [
        'discountType',
        'discount',
        'startDate',
        'endDate',
      ]);
      return this.http.put<ApiResponse<unknown>>(
        `${this.baseUrl}/discounts/${discountId}`,
        requestBody
      );
    }
}
