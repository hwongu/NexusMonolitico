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

import { ProductsService } from '../../core/services/products.service';
import { Product } from '../../core/models/product.model';

@Component({
    selector: 'app-products',
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
    templateUrl: './products.component.html',
    styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit, AfterViewInit {

  private productsService = inject(ProductsService);
  private router = inject(Router);

  displayedColumns: string[] = ['idProducto', 'nombreCategoria', 'nombre', 'precio', 'stock', 'acciones'];
  dataSource = new MatTableDataSource<Product>();
  message: string | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    this.loadProducts();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadProducts(): void {
    this.productsService.getProducts().subscribe(products => {
      this.dataSource.data = products;
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  newProduct(): void {
    this.router.navigate(['/products/new']);
  }

  editProduct(id: number): void {
    this.router.navigate(['/products/edit', id]);
  }

  deleteProduct(id: number): void {
    const confirmed = window.confirm('¿Está seguro de eliminar este producto?');

    if (!confirmed) {
      return;
    }

    this.productsService.deleteProduct(id).subscribe({
      next: () => {
        this.message = 'Producto eliminado correctamente.';
        this.loadProducts();
        setTimeout(() => this.message = null, 3000);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          this.message = 'No se encontró el producto que se intentó eliminar.';
        } else if (error.status === 409) {
          this.message = 'No se puede eliminar el producto porque está referenciado en un ingreso.';
        } else {
          this.message = 'Ocurrió un error al eliminar el producto.';
        }

        setTimeout(() => this.message = null, 3000);
      }
    });
  }

}
