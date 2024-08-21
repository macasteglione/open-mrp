import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule],
  templateUrl: "pagination.component.html",
  styleUrl: "pagination.component.css"
})
export class PaginationComponent {
  @Input() totalPages: number = 0;
  @Input() last: boolean = false;
  @Input() currentPage: number = 1;
  @Input() number: number = 1;
  @Output() pageChangeRequested = new EventEmitter<number>();
  pages: number[] = [];

  constructor() { }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["totalPages"])
      this.pages = Array.from(Array(this.totalPages).keys());
  }

  onPageChange(pageId: number): void {
    if (this.totalPages <= 1)
      return;
    if (!this.currentPage) this.currentPage = 1;
    let page = pageId;
    if (page === -2) page = 1;
    if (pageId === -1)
      page = this.currentPage > 1 ? this.currentPage - 1 : 1;
    if (pageId === -3)
      page = !this.last ? this.currentPage + 1 : this.currentPage;
    if (pageId === -4) page = this.totalPages;
    if (pageId > 1 && this.pages.length >= pageId)
      page = this.pages[pageId - 1] + 1;
    if (page === this.totalPages)
      this.last = true;
    else this.last = false;
    this.currentPage = page;
    this.pageChangeRequested.emit(page);
  }
}
