import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { pickBody, toQueryParams } from '../../../core/api/request.utils';
import { ApiResponse } from '../../../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class Employee {
  private readonly http = inject(HttpClient);

  // All employee requests start with this API path.
  private readonly baseUrl = '/api/employees';

  // The shared endpoint runner calls this method to dispatch one employee action.
  execute(endpointId: string, values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    switch (endpointId) {
      case 'listEmployees':
        return this.getAllEmployees(values);
      case 'getEmployeeById':
        return this.getEmployeeById(values);
      case 'createEmployee':
        return this.createEmployee(values);
      case 'updateEmployee':
        return this.updateEmployee(values);
      case 'deleteEmployee':
        return this.deleteEmployee(values);
      case 'employeesByPublisher':
        return this.getEmployeesByPublisher(values);
      case 'listJobs':
        return this.getAllJobs(values);
      case 'createJob':
        return this.createJob(values);
      case 'getJobById':
        return this.getJobById(values);
      case 'updateJob':
        return this.updateJob(values);
      default:
        throw new Error(`Unknown employee endpoint: ${endpointId}`);
    }
  }

  private getAllEmployees(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(this.baseUrl, {
      params: toQueryParams(values, ['pubId', 'jobId', 'page', 'size', 'sort']),
    });
  }

  private getEmployeeById(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const employeeId = values['empId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/${employeeId}`);
  }

  private createEmployee(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const requestBody = pickBody(values, [
      'empId',
      'fname',
      'minit',
      'lname',
      'jobId',
      'jobLvl',
      'pubId',
      'hireDate',
    ]);

    return this.http.post<ApiResponse<unknown>>(this.baseUrl, requestBody);
  }

  private updateEmployee(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const employeeId = values['empId'];
    const requestBody = pickBody(values, [
      'fname',
      'minit',
      'lname',
      'jobId',
      'jobLvl',
      'pubId',
      'hireDate',
    ]);

    return this.http.put<ApiResponse<unknown>>(`${this.baseUrl}/${employeeId}`, requestBody);
  }

  private deleteEmployee(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const employeeId = values['empId'];
    return this.http.delete<ApiResponse<unknown>>(`${this.baseUrl}/${employeeId}`);
  }

  private getEmployeesByPublisher(
    values: Record<string, unknown>,
  ): Observable<ApiResponse<unknown>> {
    const publisherId = values['pubId'];
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/publisher/${publisherId}`);
  }

  private getAllJobs(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/jobs`, {
      params: toQueryParams(values, ['page', 'size', 'sort']),
    });
  }

  private createJob(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
    const requestBody = pickBody(values, ['jobDesc', 'minLvl', 'maxLvl']);
    return this.http.post<ApiResponse<unknown>>(`${this.baseUrl}/jobs`, requestBody);
  }

private getJobById(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
  const jobId = values['jobId'];
  return this.http.get<ApiResponse<unknown>>(`${this.baseUrl}/jobs/${jobId}`);
}

private updateJob(values: Record<string, unknown>): Observable<ApiResponse<unknown>> {
  const jobId = values['jobId'];

  const requestBody = pickBody(values, ['jobDesc', 'minLvl', 'maxLvl']);

  return this.http.put<ApiResponse<unknown>>(
    `${this.baseUrl}/jobs/${jobId}`,
    requestBody
  );
}
}
