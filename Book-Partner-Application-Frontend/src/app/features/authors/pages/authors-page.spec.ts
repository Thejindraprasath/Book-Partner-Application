import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorsPage } from './authors-page';

describe('AuthorsPage', () => {
  let component: AuthorsPage;
  let fixture: ComponentFixture<AuthorsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthorsPage],
    }).compileComponents();

    fixture = TestBed.createComponent(AuthorsPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
