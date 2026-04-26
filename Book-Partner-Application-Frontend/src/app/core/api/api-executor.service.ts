import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Author } from '../../features/authors/services/author';
import { BookService } from '../../features/books/services/book';
import { Employee } from '../../features/employees/services/employee';
import { Publisher } from '../../features/publishers/services/publisher';
import { Sales } from '../../features/sales/services/sales';
import { Store } from '../../features/stores/services/store';

@Injectable({
  providedIn: 'root',
})
export class ApiExecutorService {
  private readonly storeService = inject(Store);
  private readonly authorService = inject(Author);
  private readonly bookService = inject(BookService);
  private readonly publisherService = inject(Publisher);
  private readonly employeeService = inject(Employee);
  private readonly salesService = inject(Sales);

  // Send the request to the correct feature service based on the active module.
  execute(moduleId: string, endpointId: string, formValue: Record<string, unknown>): Observable<unknown> {
    switch (moduleId) {
      case 'store':
        return this.storeService.execute(endpointId, formValue);
      case 'author':
        return this.authorService.execute(endpointId, formValue);
      case 'book':
        return this.bookService.execute(endpointId, formValue);
      case 'publisher':
        return this.publisherService.execute(endpointId, formValue);
      case 'employee':
        return this.employeeService.execute(endpointId, formValue);
      case 'sales':
        return this.salesService.execute(endpointId, formValue);
      default:
        throw new Error(`Unknown module: ${moduleId}`);
    }
  }
}
