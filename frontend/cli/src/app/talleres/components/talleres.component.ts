import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PaginationComponent } from '../../pagination/pagination.component';
import { ResultsPage } from '../../results-page';
import { TalleresService } from '../interfaces/talleres.service';

@Component({
  selector: 'app-talleres',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: "talleres.component.html",
  styleUrl: "talleres.component.css"
})
export class TalleresComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  pageSize: number = 10;

  constructor(private tallerService: TalleresService) { }

  getBorderos(): void {
    this.tallerService
      .byPage(this.currentPage, this.pageSize)
      .subscribe(
        (dataPackage) => {
          this.resultsPage = <ResultsPage>dataPackage.data;
        }
      );
  }

  ngOnInit(): void {
    this.getBorderos();
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getBorderos();
  }
}
