import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublishersPage } from './publishers-page';

describe('PublishersPage', () => {
  let component: PublishersPage;
  let fixture: ComponentFixture<PublishersPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublishersPage],
    }).compileComponents();

    fixture = TestBed.createComponent(PublishersPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
