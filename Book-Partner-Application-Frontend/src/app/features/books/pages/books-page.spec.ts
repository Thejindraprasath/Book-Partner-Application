import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BooksPage } from './books-page';

// Basic smoke test for the books page component.
describe('BooksPage', () => {
  let component: BooksPage;
  let fixture: ComponentFixture<BooksPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BooksPage],
    }).compileComponents();

    fixture = TestBed.createComponent(BooksPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
