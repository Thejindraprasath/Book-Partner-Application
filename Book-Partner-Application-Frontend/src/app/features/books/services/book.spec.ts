import { TestBed } from '@angular/core/testing';

import { BookService } from './book';

// Basic smoke test for the book service.
describe('BookService', () => {
  let service: BookService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
