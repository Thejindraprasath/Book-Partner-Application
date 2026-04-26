import { TestBed } from '@angular/core/testing';

import { Employee } from './employee';

// Basic smoke test for the employee service.
describe('Employee', () => {
  let service: Employee;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Employee);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
