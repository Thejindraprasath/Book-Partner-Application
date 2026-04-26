import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { ApiExecutorService } from '../../../core/api/api-executor.service';
import { EndpointDefinition, FormFieldDefinition } from '../../../models/module.model';
import { Pagination } from '../pagination/pagination';
import { Table } from '../table/table';
import {
  ENTITY_LABELS,
  FIELD_MAX_MESSAGES,
  FIELD_MAX_VALUES,
  FIELD_MIN_MESSAGES,
  FIELD_MIN_VALUES,
  FIELD_PATTERNS,
  FIELD_PATTERN_MESSAGES,
  FIELD_PLACEHOLDERS,
} from './endpoint-runner.data';

@Component({
  selector: 'app-endpoint-runner',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, Pagination, Table],
  templateUrl: './endpoint-runner.html',
  styleUrls: ['./endpoint-runner.css'],
})
// Shared page that renders one API action form and shows the result.
export class EndpointRunner implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly formBuilder = inject(FormBuilder);
  private readonly apiExecutor = inject(ApiExecutorService);

  // These values come from the route definition for the current endpoint page.
  readonly moduleId = this.route.snapshot.data['moduleId'] as string;
  readonly moduleRoute = this.route.snapshot.data['moduleRoute'] as string;
  readonly endpoint = this.route.snapshot.data['endpoint'] as EndpointDefinition;

  // The form is built from the endpoint config, so one page can handle many APIs.
  readonly form = this.buildForm();

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
  readonly errorDetails = computed(() => this.objectEntries(this.rawError()));
  readonly isListView = computed(() => this.endpoint.id.startsWith('list'));
  readonly isMutationEndpoint = computed(() =>
    ['POST', 'PUT', 'DELETE'].includes(this.endpoint.method),
  );

  readonly tableRows = computed(() => {
    const responseData = this.responseData();
    const rows = this.extractRows(responseData);

    if (rows.length > 0) {
      return rows.map((row) => this.toTableRow(row));
    }

    if (this.isPlainObject(responseData)) {
      return [responseData];
    }

    if (this.endpoint.method === 'DELETE' && this.responseMessage()) {
      return [this.buildDeleteSummaryRow()];
    }

    return [];
  });

  readonly shouldShowTable = computed(() => this.tableRows().length > 0);
  readonly shouldShowTableSurface = computed(() => {
    const responseData = this.responseData();

    return (
      this.shouldShowTable() ||
      Array.isArray(responseData) ||
      this.hasContentArray(responseData)
    );
  });

  readonly paginationInfo = computed(() => {
    const responseData = this.responseData();

    if (!this.hasContentArray(responseData)) {
      return null;
    }

    return {
      pageNumber: Number(responseData['pageNumber']),
      totalPages: Number(responseData['totalPages']),
      totalElements: Number(responseData['totalElements']),
      first: Boolean(responseData['first']),
      last: Boolean(responseData['last']),
    };
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
    // List endpoints can load immediately with default filters.
    if (!this.isListView()) {
      return;
    }

    const defaultValues = this.defaultRequestValues();
    this.lastSubmittedValues.set(defaultValues);
    this.runRequest(defaultValues);
  }

  submit(): void {
    this.clearRequestState();

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    // Turn the form into a clean payload and send the request.
    const formValues = this.normalizeValues();
    this.lastSubmittedValues.set(formValues);
    this.runRequest(formValues);
  }

  fieldType(field: FormFieldDefinition): string {
    return field.type === 'number' ? 'number' : field.type;
  }

  fieldPlaceholder(field: FormFieldDefinition): string {
    return field.placeholder ?? FIELD_PLACEHOLDERS[field.name] ?? `Enter ${field.label.toLowerCase()}`;
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
      return FIELD_PATTERN_MESSAGES[field.name] ?? `Enter a valid ${field.label.toLowerCase()}.`;
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
    const pageInfo = this.paginationInfo();

    if (!pageInfo || pageInfo.last) {
      return;
    }

    this.goToPage(pageInfo.pageNumber + 1);
  }

  goToPreviousPage(): void {
    const pageInfo = this.paginationInfo();

    if (!pageInfo || pageInfo.first) {
      return;
    }

    this.goToPage(Math.max(pageInfo.pageNumber - 1, 0));
  }

  goToPage(pageNumber: number): void {
    const currentValues = this.lastSubmittedValues();
    const pageInfo = this.paginationInfo();

    if (!currentValues || !pageInfo || pageNumber < 0 || pageNumber >= pageInfo.totalPages) {
      return;
    }

    // Reuse the last submitted filters and only replace the page number.
    const nextValues = { ...currentValues, page: pageNumber };

    this.form.patchValue({ page: pageNumber });
    this.lastSubmittedValues.set(nextValues);
    this.runRequest(nextValues);
  }

  private buildForm() {
    // Build controls from the endpoint definition instead of hardcoding each form.
    const controls = Object.fromEntries(
      this.endpoint.formFields.map((field) => [
        field.name,
        [field.defaultValue ?? '', this.buildValidators(field)],
      ]),
    );

    return this.formBuilder.group(controls);
  }

  private clearRequestState(): void {
    this.errorMessage.set('');
    this.rawError.set(null);
    this.errorStatus.set(null);
    this.responseMessage.set('');
    this.rawResponse.set(null);
    this.responseStatus.set(null);
    this.responseData.set(null);
  }

  private runRequest(values: Record<string, unknown>): void {
    this.isLoading.set(true);

    // The executor chooses the correct feature service behind the scenes.
    this.apiExecutor.execute(this.moduleId, this.endpoint.id, values).subscribe({
      next: (response) => this.handleSuccess(response),
      error: (error: HttpErrorResponse) => this.handleError(error, values),
    });
  }

  private handleSuccess(response: unknown): void {
    // Store both the raw response and the extracted display data.
    const responseBody = this.unwrapResponse(response);

    this.rawResponse.set(response ?? null);
    this.responseStatus.set(this.extractStatus(response));
    this.responseMessage.set(this.extractResponseMessage(responseBody));
    this.responseData.set(this.extractResponseData(responseBody, response));
    this.isLoading.set(false);
  }

  private handleError(error: HttpErrorResponse, values: Record<string, unknown>): void {
    // Keep a readable message for the UI and preserve raw error details for debugging.
    this.errorStatus.set(error.status || null);
    this.rawError.set(error.error ?? { message: error.message, status: error.status });
    this.errorMessage.set(this.buildErrorMessage(error, values));
    this.isLoading.set(false);
  }

  private extractResponseMessage(responseBody: unknown): string {
    if (this.isPlainObject(responseBody) && typeof responseBody['message'] === 'string') {
      return responseBody['message'];
    }

    return 'Request completed successfully.';
  }

  private extractResponseData(responseBody: unknown, originalResponse: unknown): unknown {
    if (this.isPlainObject(responseBody) && 'data' in responseBody) {
      return responseBody['data'];
    }

    return responseBody ?? originalResponse;
  }

  private buildValidators(field: FormFieldDefinition): ValidatorFn[] {
    const validators: ValidatorFn[] = [];

    if (field.required) {
      validators.push(Validators.required);
    }

    const pattern = FIELD_PATTERNS[field.name];
    if (pattern) {
      validators.push(Validators.pattern(pattern));
    }

    // Number fields always have a minimum so blank and negative values are controlled consistently.
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
    return FIELD_MIN_VALUES[fieldName] ?? 0;
  }

  private maxValueForField(fieldName: string): number | null {
    return FIELD_MAX_VALUES[fieldName] ?? null;
  }

  private minValueMessage(fieldName: string): string {
    return FIELD_MIN_MESSAGES[fieldName] ?? '0 or greater';
  }

  private maxValueMessage(fieldName: string): string {
    return FIELD_MAX_MESSAGES[fieldName] ?? 'within the allowed range';
  }

  private buildErrorMessage(error: HttpErrorResponse, values: Record<string, unknown>): string {
    const backendMessage = String(
      error.error?.message ?? error.error?.error ?? error.message ?? 'Request failed.',
    );
    const lowerCaseMessage = backendMessage.toLowerCase();

    // Some expired-session responses come back as HTML instead of JSON, so detect those too.
    if (
      error.status === 401 ||
      lowerCaseMessage.includes('authentication required') ||
      lowerCaseMessage.includes('unexpected token') ||
      lowerCaseMessage.includes('<!doctype html>')
    ) {
      return 'Session expired. Please login again.';
    }

    if (error.status === 404 || lowerCaseMessage.includes('not found')) {
      return this.notFoundMessage(values);
    }

    return backendMessage;
  }

  private notFoundMessage(values: Record<string, unknown>): string {
    const identifierText = this.identifierSummary(values);

    if (identifierText) {
      return `${this.entityLabel()} not found with ${identifierText}.`;
    }

    return `${this.entityLabel()} not found.`;
  }

  private entityLabel(): string {
    return ENTITY_LABELS[this.moduleId] ?? 'Data';
  }

  private endpointResourceLabel(): string {
    return (
      this.endpoint.title
        .replace(/^Get All\s+/i, '')
        .replace(/^Get\s+/i, '')
        .replace(/^Create\s+/i, '')
        .replace(/^Update\s+/i, '')
        .replace(/^Delete\s+/i, '')
        .replace(/\s+By\s+.*$/i, '')
        .trim() || this.entityLabel()
    );
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
    // DELETE responses often have no record body, so show a small summary instead.
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

  private unwrapResponse(response: unknown): unknown {
    if (this.isPlainObject(response) && 'body' in response && 'status' in response) {
      return response['body'] ?? null;
    }

    return response;
  }

  private extractStatus(response: unknown): number | null {
    if (this.isPlainObject(response) && 'status' in response) {
      return Number(response['status']);
    }

    return this.endpoint.method === 'GET' ? 200 : null;
  }

  private extractRows(value: unknown): unknown[] {
    // Support both plain arrays and paged backend responses with a content array.
    if (Array.isArray(value)) {
      return value;
    }

    if (this.hasContentArray(value)) {
      return value['content'] as unknown[];
    }

    return [];
  }

  private toTableRow(row: unknown): Record<string, unknown> {
    if (this.isPlainObject(row)) {
      return row;
    }

    return { value: row };
  }

  private objectEntries(value: unknown): Array<{ key: string; value: string }> {
    if (!this.isPlainObject(value)) {
      return [];
    }

    return Object.entries(value).map(([key, entryValue]) => ({
      key,
      value: this.formatDisplayValue(entryValue),
    }));
  }

  private formatDisplayValue(value: unknown): string {
    if (value === null || value === undefined) {
      return '-';
    }

    if (typeof value === 'string' || typeof value === 'number' || typeof value === 'boolean') {
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

  private isPlainObject(value: unknown): value is Record<string, any> {
    return !!value && typeof value === 'object' && !Array.isArray(value);
  }

  private hasContentArray(value: unknown): value is Record<string, unknown> {
    return this.isPlainObject(value) && Array.isArray(value['content']);
  }
}
