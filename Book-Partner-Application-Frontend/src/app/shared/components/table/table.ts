import { CommonModule } from '@angular/common';
import { Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-table',
  imports: [CommonModule],
  templateUrl: './table.html',
  styleUrl: './table.css',
})
export class Table {
  readonly rows = input<unknown[]>([]);
  readonly emptyMessage = input('No records found.');

  readonly columns = computed(() => {
    const columnNames = new Set<string>();

    for (const row of this.rows()) {
      if (!row || typeof row !== 'object' || Array.isArray(row)) {
        continue;
      }

      for (const key of Object.keys(row as Record<string, unknown>)) {
        columnNames.add(key);
      }
    }

    return Array.from(columnNames);
  });

  formatValue(value: unknown): string {
    if (value === null || value === undefined || value === '') {
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
}
