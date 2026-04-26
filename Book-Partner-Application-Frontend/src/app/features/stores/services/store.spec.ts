import { TestBed } from '@angular/core/testing';

import { Store } from './store';

// Basic smoke test for the store service.
describe('Store', () => {
  let service: Store;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Store);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
