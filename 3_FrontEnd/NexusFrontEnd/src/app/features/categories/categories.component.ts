import { Component, OnInit, inject, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

import { CategoriesService } from '../../core/services/categories.service';
import { Category } from '../../core/models/category.model';

@Component({
    selector: 'app-categories',
    imports: [
        CommonModule,
        RouterModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        MatButtonModule
    ],
    templateUrl: './categories.component.html',
    styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit, AfterViewInit {

  private categoriesService = inject(CategoriesService);
  private router = inject(Router);

  displayedColumns: string[] = ['idCategoria', 'nombre', 'descripcion', 'acciones'];
  dataSource = new MatTableDataSource<Category>();
  message: string | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    this.loadCategories();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadCategories(): void {
    this.categoriesService.getCategories().subscribe(categories => {
      this.dataSource.data = categories;
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  newCategory(): void {
    this.router.navigate(['/categories/new']);
  }

  editCategory(id: number): void {
    this.router.navigate(['/categories/edit', id]);
  }

  deleteCategory(id: number): void {
    const confirmed = window.confirm('¿Está seguro de eliminar esta categoría?');

    if (!confirmed) {
      return;
    }

    this.categoriesService.deleteCategory(id).subscribe({
      next: () => {
        this.message = 'Categoría eliminada correctamente.';
        this.loadCategories();
        setTimeout(() => this.message = null, 3000);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          this.message = 'No se encontró la categoría que se intentó eliminar.';
        } else if (error.status === 409) {
          this.message = 'No se puede eliminar la categoría porque tiene productos asociados.';
        } else {
          this.message = 'Ocurrió un error al eliminar la categoría.';
        }

        setTimeout(() => this.message = null, 3000);
      }
    });
  }
}
