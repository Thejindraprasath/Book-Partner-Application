import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeesPage } from './employees-page';

// Basic smoke test for the employees page component.
describe('EmployeesPage', () => {
  let component: EmployeesPage;
  let fixture: ComponentFixture<EmployeesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeesPage],
    }).compileComponents();

    fixture = TestBed.createComponent(EmployeesPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
