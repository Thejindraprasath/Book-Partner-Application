import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { ApiExecutorService } from '../../../core/api/api-executor.service';
import { getEndpointsForModule } from '../../../config/api.config';
import { EndpointDefinition, FormFieldDefinition } from '../../../models/module.model';
import { Pagination } from '../pagination/pagination';
import { Table } from '../table/table';

@Component({
  selector: 'app-endpoint-runner',
  imports: [CommonModule, ReactiveFormsModule, RouterLink, Pagination, Table],
  templateUrl: './endpoint-runner.html',
  styleUrl: './endpoint-runner.css',
})
export class EndpointRunner {
  private readonly route = inject(ActivatedRoute);
  private readonly formBuilder = inject(FormBuilder);
  private readonly apiExecutor = inject(ApiExecutorService);

  readonly moduleId = this.route.snapshot.data['moduleId'] as string;
  readonly moduleRoute = this.route.snapshot.data['moduleRoute'] as string;
  readonly endpoint = this.route.snapshot.data['endpoint'] as EndpointDefinition;

  readonly form = this.formBuilder.group(
    Object.fromEntries(
      this.endpoint.formFields.map((field) => [
        field.name,
        [
          field.defaultValue ?? '',
          this.buildValidators(field),
        ],
      ])
    )
  );

  readonly isLoading = signal(false);
  readonly rawResponse = signal<unknown>(null);
  readonly rawError = signal<unknown>(null);
  readonly responseStatus = signal<number | null>(null);
  readonly errorStatus = signal<number | null>(null);
  readonly responseData = signal<unknown>(null);
  readonly responseMessage = signal('');
  readonly errorMessage = signal('');
  readonly lastSubmittedValues = signal<Record<string, unknown> | null>(null);

  readonly responseJson = computed(() => this.formatJson(this.rawResponse() ?? this.responseData()));
  readonly errorJson = computed(() => this.formatJson(this.rawError()));
  readonly isListView = computed(() => this.isListEndpoint());
  readonly errorDetails = computed(() => this.objectEntries(this.rawError()));

  readonly responseKind = computed(() => {
    const response = this.rawResponse() ?? this.responseData();
    if (Array.isArray(response)) {
      return 'Array';
    }

    if (response && typeof response === 'object') {
      return 'Object';
    }

    return typeof response;
  });

  readonly tableRows = computed(() => {
    const responseData = this.responseData();
    const rows = this.extractRows(responseData);

    if (!rows.length && responseData && typeof responseData === 'object' && !Array.isArray(responseData)) {
      return [responseData];
    }

    if (!rows.length && this.endpoint.method === 'DELETE' && this.responseMessage()) {
      return [this.buildDeleteSummaryRow()];
    }

    return rows.map((row) => {
      if (row && typeof row === 'object' && !Array.isArray(row)) {
        return row;
      }

      return { value: row };
    });
  });

  readonly shouldShowTable = computed(() => this.tableRows().length > 0);
  readonly shouldShowTableSurface = computed(() => {
    const responseData = this.responseData();

    return this.shouldShowTable()
      || Array.isArray(responseData)
      || !!(responseData && typeof responseData === 'object' && Array.isArray((responseData as any).content));
  });

  readonly paginationInfo = computed(() => {
    const responseData = this.responseData();
    if (!responseData || typeof responseData !== 'object') {
      return null;
    }

    const value = responseData as Record<string, unknown>;
    if (!Array.isArray(value['content'])) {
      return null;
    }

    return {
      pageNumber: Number(value['pageNumber']),
      totalPages: Number(value['totalPages']),
      totalElements: Number(value['totalElements']),
      first: Boolean(value['first']),
      last: Boolean(value['last']),
    };
  });

  readonly supportsPagination = computed(() =>
    !!this.paginationInfo()
  );

  readonly isMutationEndpoint = computed(() =>
    this.endpoint.method === 'POST' || this.endpoint.method === 'PUT' || this.endpoint.method === 'DELETE'
  );

  readonly singleResponseRows = computed(() => {
    if (!this.isMutationEndpoint()) {
      return [];
    }

    const responseData = this.responseData();
    if (responseData && typeof responseData === 'object' && !Array.isArray(responseData)) {
      return [responseData];
    }

    if (this.endpoint.method === 'DELETE') {
      return [this.buildDeleteSummaryRow()];
    }

    return [];
  });

  readonly viewAllRoute = computed(() => {
    if (!this.isMutationEndpoint()) {
      return null;
    }

    const moduleEndpoints = getEndpointsForModule(this.moduleId);
    const listEndpoint = moduleEndpoints.find((item) => item.id.startsWith('list'));
    if (!listEndpoint) {
      return null;
    }

    return `${this.moduleRoute}/${listEndpoint.route}`;
  });

  readonly responseTitle = computed(() => {
    if (this.isListView()) {
      return `${this.endpointResourceLabel()} fetched successfully`;
    }

    if (this.isMutationEndpoint()) {
      return `${this.endpointResourceLabel()} request completed successfully`;
    }

    return `${this.endpointResourceLabel()} details fetched successfully`;
  });

  readonly dataSectionTitle = computed(() => {
    if (this.shouldShowTableSurface()) {
      return this.isListView() ? 'All Records' : 'Data';
    }

    return 'Details';
  });

  ngOnInit(): void {
    if (this.isListEndpoint()) {
      const defaultValues = this.defaultRequestValues();
      this.lastSubmittedValues.set(defaultValues);
      this.runRequest(defaultValues);
    }
  }

  submit(): void {
    this.errorMessage.set('');
    this.rawError.set(null);
    this.errorStatus.set(null);
    this.responseMessage.set('');
    this.rawResponse.set(null);
    this.responseStatus.set(null);
    this.responseData.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const normalizedValues = this.normalizeValues();
    this.lastSubmittedValues.set(normalizedValues);
    this.runRequest(normalizedValues);
  }

  fieldType(field: FormFieldDefinition): string {
    return field.type === 'number' ? 'number' : field.type;
  }

  fieldPlaceholder(field: FormFieldDefinition): string {
    if (field.placeholder) {
      return field.placeholder;
    }

    switch (field.name) {
      case 'storeId':
      case 'storId':
        return 'Enter store ID (4 digits), e.g. 7066';
      case 'auId':
        return 'Enter author ID, e.g. 409-56-7008';
      case 'pubId':
        return 'Enter publisher ID (4 digits), e.g. 1389';
      case 'titleId':
      case 'id':
        return 'Enter title ID, e.g. BU1032';
      case 'empId':
        return 'Enter employee ID, e.g. PTC11962M';
      case 'ordNum':
        return 'Enter order number, e.g. 423LL930';
      case 'city':
        return 'Enter city, e.g. Oakland';
      case 'state':
        return 'Enter state code, e.g. CA';
      case 'zip':
        return 'Enter ZIP code (5 digits), e.g. 94705';
      case 'phone':
        return 'Enter phone, e.g. 415 658-9932';
      case 'contract':
        return 'Enter contract value, e.g. 1';
      case 'type':
        return 'Enter type, e.g. business';
      case 'price':
        return 'Enter price, e.g. 19.99';
      case 'advance':
        return 'Enter advance, e.g. 5000';
      case 'royalty':
        return 'Enter royalty, e.g. 10';
      case 'ytdSales':
        return 'Enter YTD sales, e.g. 4095';
      case 'jobId':
        return 'Enter job ID, e.g. 5';
      case 'jobLvl':
        return 'Enter job level, e.g. 175';
      case 'minLvl':
        return 'Enter minimum level, e.g. 10';
      case 'maxLvl':
        return 'Enter maximum level, e.g. 250';
      case 'qty':
        return 'Enter quantity, e.g. 10';
      case 'payterms':
        return 'Enter pay terms, e.g. Net 60';
      case 'page':
        return 'Enter page number, e.g. 0';
      case 'size':
        return 'Enter page size, e.g. 10';
      case 'direction':
        return 'Enter asc or desc';
      case 'sortBy':
        return 'Enter sort field, e.g. ordDate';
      case 'sort':
        return 'Enter sort, e.g. title,asc';
      case 'country':
        return 'Enter country, e.g. USA';
      case 'minit':
        return 'Enter one letter, e.g. T';
      case 'from':
        return 'Select start date and time';
      case 'to':
        return 'Select end date and time';
      case 'pubdate':
      case 'ordDate':
      case 'hireDate':
        return 'Select date and time';
      default:
        return `Enter ${field.label.toLowerCase()}`;
    }
  }

  controlInvalid(fieldName: string): boolean {
    const control = this.form.get(fieldName);
    return !!control && control.invalid && control.touched;
  }

  validationMessage(field: FormFieldDefinition): string {
    const control = this.form.get(field.name);
    if (!control || !control.errors || !control.touched) {
      return '';
    }

    if (control.errors['required']) {
      return `${field.label} is required.`;
    }

    if (control.errors['pattern']) {
      return this.patternMessage(field.name, field.label);
    }

    if (control.errors['min']) {
      return `${field.label} must be ${this.minValueMessage(field.name)}.`;
    }

    if (control.errors['max']) {
      return `${field.label} must be ${this.maxValueMessage(field.name)}.`;
    }

    return `Enter a valid ${field.label.toLowerCase()}.`;
  }

  goToNextPage(): void {
    const currentValues = this.lastSubmittedValues();
    const pagination = this.paginationInfo();

    if (!currentValues || !pagination || pagination.last) {
      return;
    }

    this.goToPage(pagination.pageNumber + 1);
  }

  goToPreviousPage(): void {
    const currentValues = this.lastSubmittedValues();
    const pagination = this.paginationInfo();

    if (!currentValues || !pagination || pagination.first) {
      return;
    }

    this.goToPage(Math.max(pagination.pageNumber - 1, 0));
  }

  goToPage(pageNumber: number): void {
    const currentValues = this.lastSubmittedValues();
    const pagination = this.paginationInfo();

    if (!currentValues || !pagination || pageNumber < 0 || pageNumber >= pagination.totalPages) {
      return;
    }

    const nextValues = {
      ...currentValues,
      page: pageNumber,
    };

    this.form.patchValue({ page: pageNumber });
    this.lastSubmittedValues.set(nextValues);
    this.runRequest(nextValues);
  }

  private runRequest(values: Record<string, unknown>): void {
    this.isLoading.set(true);

    this.apiExecutor.execute(this.moduleId, this.endpoint.id, values).subscribe({
      next: (response: any) => {
        const envelope = this.unwrapResponse(response);
        this.rawResponse.set(response ?? null);
        this.responseStatus.set(this.extractStatus(response));
        this.responseMessage.set(envelope?.message ?? 'Request completed successfully.');
        this.responseData.set(envelope?.data ?? envelope ?? response);
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.errorStatus.set(error.status || null);
        this.rawError.set(error.error ?? { message: error.message, status: error.status });
        this.errorMessage.set(this.buildErrorMessage(error, values));
        this.isLoading.set(false);
      },
    });
  }

  private buildValidators(field: FormFieldDefinition): ValidatorFn[] {
    const validators: ValidatorFn[] = [];

    if (field.required) {
      validators.push(Validators.required);
    }

    const pattern = this.validationPattern(field.name);
    if (pattern) {
      validators.push(Validators.pattern(pattern));
    }

    if (field.type === 'number') {
      validators.push(Validators.min(this.minValueForField(field.name)));
    }

    const maxValue = this.maxValueForField(field.name);
    if (maxValue !== null) {
      validators.push(Validators.max(maxValue));
    }

    return validators;
  }

  private minValueForField(fieldName: string): number {
    switch (fieldName) {
      case 'price':
        return 0.01;
      default:
        return 0;
    }
  }

  private maxValueForField(fieldName: string): number | null {
    switch (fieldName) {
      case 'royalty':
        return 100;
      case 'contract':
        return 1;
      default:
        return null;
    }
  }

  private minValueMessage(fieldName: string): string {
    switch (fieldName) {
      case 'price':
        return 'greater than 0';
      case 'royalty':
        return 'between 0 and 100';
      case 'contract':
        return '0 or 1';
      default:
        return '0 or greater';
    }
  }

  private maxValueMessage(fieldName: string): string {
    switch (fieldName) {
      case 'royalty':
        return 'between 0 and 100';
      case 'contract':
        return '0 or 1';
      default:
        return 'within the allowed range';
    }
  }

  private validationPattern(fieldName: string): RegExp | null {
    switch (fieldName) {
      case 'storeId':
      case 'storId':
      case 'pubId':
        return /^\d{4}$/;
      case 'auId':
        return /^\d{3}-\d{2}-\d{4}$/;
      case 'zip':
        return /^\d{5}$/;
      case 'state':
        return /^[A-Z]{2}$/;
      case 'titleId':
      case 'id':
        return /^[A-Z]{2}\d{4}$|^[A-Z]{2}\d{4,6}$|^[A-Z]{2,3}\d{4}$/;
      case 'empId':
        return /^([A-Z]{3}[1-9][0-9]{4}[FM]|[A-Z]-[A-Z][1-9][0-9]{4}[FM])$/;
      case 'minit':
        return /^[A-Za-z]$/;
      case 'contract':
        return /^[01]$/;
      default:
        return null;
    }
  }

  private patternMessage(fieldName: string, label: string): string {
    switch (fieldName) {
      case 'storeId':
      case 'storId':
        return 'Store ID must be exactly 4 digits, e.g. 7066.';
      case 'pubId':
        return 'Publisher ID must be exactly 4 digits, e.g. 1389.';
      case 'auId':
        return 'Author ID must match 999-99-9999 format.';
      case 'zip':
        return 'ZIP code must be exactly 5 digits.';
      case 'state':
        return 'State must be a 2-letter uppercase code, e.g. CA.';
      case 'titleId':
      case 'id':
        return 'Title ID should look like BU1032, PS2091, or TC7777.';
      case 'empId':
        return 'Employee ID must match a valid format, e.g. PTC11962M.';
      case 'minit':
        return 'Middle Initial must be a single letter.';
      case 'contract':
        return 'Contract must be 0 or 1.';
      default:
        return `Enter a valid ${label.toLowerCase()}.`;
    }
  }

  private buildErrorMessage(error: HttpErrorResponse, values: Record<string, unknown>): string {
    const rawBackendMessage = error.error?.message ?? error.error?.error ?? error.message ?? 'Request failed.';
    const backendMessage = String(rawBackendMessage);
    const lowerCaseMessage = backendMessage.toLowerCase();

    if (error.status === 401 || lowerCaseMessage.includes('authentication required')) {
      return 'Session expired. Please login again.';
    }

    if (lowerCaseMessage.includes('unexpected token') || lowerCaseMessage.includes('<!doctype html>')) {
      return 'Session expired. Please login again.';
    }

    if (error.status === 404) {
      return this.notFoundMessage(values);
    }

    if (lowerCaseMessage.includes('not found')) {
      return this.notFoundMessage(values);
    }

    return backendMessage;
  }

  private notFoundMessage(values: Record<string, unknown>): string {
    const entityName = this.entityLabel();
    const identifierText = this.identifierSummary(values);

    if (identifierText) {
      return `${entityName} not found with ${identifierText}.`;
    }

    return `${entityName} not found.`;
  }

  private entityLabel(): string {
    switch (this.moduleId) {
      case 'store':
        return 'Store';
      case 'author':
        return 'Author';
      case 'book':
        return 'Book';
      case 'publisher':
        return 'Publisher';
      case 'employee':
        return 'Employee';
      case 'sales':
        return 'Sale';
      default:
        return 'Data';
    }
  }

  private endpointResourceLabel(): string {
    return this.endpoint.title
      .replace(/^Get All\s+/i, '')
      .replace(/^Get\s+/i, '')
      .replace(/^Create\s+/i, '')
      .replace(/^Update\s+/i, '')
      .replace(/^Delete\s+/i, '')
      .replace(/\s+By\s+.*$/i, '')
      .trim() || this.entityLabel();
  }

  private identifierSummary(values: Record<string, unknown>): string {
    const parts = this.endpoint.formFields
      .filter((field) => field.location === 'path')
      .map((field) => {
        const value = values[field.name];
        if (value === null || value === undefined || value === '') {
          return '';
        }

        return `${field.label} = ${value}`;
      })
      .filter(Boolean);

    return parts.join(', ');
  }

  private normalizeValues(): Record<string, unknown> {
    const rawValue = this.form.getRawValue() as Record<string, unknown>;
    const normalized: Record<string, unknown> = {};

    for (const field of this.endpoint.formFields) {
      const value = rawValue[field.name];
      if (value === '' || value === null || value === undefined) {
        continue;
      }

      normalized[field.name] = field.type === 'number' ? Number(value) : value;
    }

    return normalized;
  }

  private isListEndpoint(): boolean {
    return this.endpoint.id.startsWith('list');
  }

  private defaultRequestValues(): Record<string, unknown> {
    const defaults: Record<string, unknown> = {};

    for (const field of this.endpoint.formFields) {
      if (field.defaultValue !== undefined) {
        defaults[field.name] = field.defaultValue;
      }
    }

    return defaults;
  }

  private buildDeleteSummaryRow(): Record<string, unknown> {
    const values = this.lastSubmittedValues() ?? {};
    const row: Record<string, unknown> = {
      action: 'Deleted',
      entity: this.entityLabel(),
    };

    for (const field of this.endpoint.formFields) {
      const value = values[field.name];
      if (value === null || value === undefined || value === '') {
        continue;
      }

      row[field.label] = value;
    }

    return row;
  }

  private unwrapResponse(response: unknown): any {
    if (response && typeof response === 'object' && 'body' in response && 'status' in response) {
      return (response as any).body ?? null;
    }

    return response;
  }

  private extractStatus(response: unknown): number | null {
    if (response && typeof response === 'object' && 'status' in response) {
      return Number((response as any).status);
    }

    return this.endpoint.method === 'GET' ? 200 : null;
  }

  private extractRows(value: unknown): unknown[] {
    if (Array.isArray(value)) {
      return value;
    }

    if (value && typeof value === 'object' && Array.isArray((value as any).content)) {
      return (value as any).content as unknown[];
    }

    return [];
  }

  private objectEntries(value: unknown): Array<{ key: string; value: string }> {
    if (!value || typeof value !== 'object' || Array.isArray(value)) {
      return [];
    }

    return Object.entries(value as Record<string, unknown>).map(([key, entryValue]) => ({
      key,
      value: this.formatDisplayValue(entryValue),
    }));
  }

  private formatDisplayValue(value: unknown): string {
    if (value === null || value === undefined) {
      return '-';
    }

    if (typeof value === 'string') {
      return value;
    }

    if (typeof value === 'number' || typeof value === 'boolean') {
      return String(value);
    }

    try {
      return JSON.stringify(value);
    } catch {
      return String(value);
    }
  }

  private formatJson(value: unknown): string {
    if (value === null || value === undefined) {
      return '';
    }

    if (typeof value === 'string') {
      return value;
    }

    try {
      return JSON.stringify(value, null, 2);
    } catch {
      return String(value);
    }
  }
}
