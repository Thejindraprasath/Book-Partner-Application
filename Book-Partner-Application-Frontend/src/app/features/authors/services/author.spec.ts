import { TestBed } from '@angular/core/testing';

import { Author } from './author';

// Basic smoke test for the author service.
describe('Author', () => {
  let service: Author;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Author);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
