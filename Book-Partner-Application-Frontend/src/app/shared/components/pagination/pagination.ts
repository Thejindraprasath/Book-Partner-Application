import { Component, computed, input, output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  imports: [],
  templateUrl: './pagination.html',
  styleUrl: './pagination.css',
})
// Reusable pagination bar for paged API responses.
export class Pagination {
  readonly pageNumber = input<number | null>(null);
  readonly totalPages = input<number | null>(null);
  readonly totalElements = input<number | null>(null);
  readonly disabled = input(false);

  readonly previousPage = output<void>();
  readonly nextPage = output<void>();
  readonly pageSelected = output<number>();

  // Show the first page, last page, and a small window around the current page.
  readonly visiblePages = computed(() => {
    const currentPage = this.pageNumber();
    const totalPages = this.totalPages();

    if (currentPage === null || totalPages === null || totalPages <= 0) {
      return [];
    }

    const pages = new Set<number>([0, totalPages - 1]);
    const start = Math.max(currentPage - 2, 0);
    const end = Math.min(currentPage + 2, totalPages - 1);

    for (let page = start; page <= end; page++) {
      pages.add(page);
    }

    return Array.from(pages)
      .sort((first, second) => first - second)
      .reduce<Array<number | 'ellipsis'>>((items, page, index, sortedPages) => {
        if (index > 0 && page - sortedPages[index - 1] > 1) {
          items.push('ellipsis');
        }

        items.push(page);
        return items;
      }, []);
  });

  isFirstPage(): boolean {
    return this.pageNumber() === 0;
  }

  isLastPage(): boolean {
    const currentPage = this.pageNumber();
    const totalPages = this.totalPages();

    return currentPage !== null && totalPages !== null && currentPage >= totalPages - 1;
  }

  selectPage(page: number | 'ellipsis'): void {
    // Ignore clicks on separators, the current page, or disabled state.
    if (page === 'ellipsis' || this.disabled() || page === this.pageNumber()) {
      return;
    }

    this.pageSelected.emit(page);
  }
}
