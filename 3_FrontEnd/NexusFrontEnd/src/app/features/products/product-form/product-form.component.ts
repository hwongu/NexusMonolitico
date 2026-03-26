import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ProductsService } from '../../../core/services/products.service';
import { CategoriesService } from '../../../core/services/categories.service';
import { Category } from '../../../core/models/category.model';

@Component({
    selector: 'app-product-form',
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatButtonModule,
        MatCardModule
    ],
    templateUrl: './product-form.component.html',
    styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private productsService = inject(ProductsService);
  private categoriesService = inject(CategoriesService);

  productForm = this.fb.group({
    idProducto: [{ value: '', disabled: true }],
    idCategoria: [null as number | null, Validators.required],
    nombre: ['', Validators.required],
    precio: [null as number | null, Validators.required],
    stock: [null as number | null, Validators.required]
  });

  categories: Category[] = [];
  isEditMode = false;
  private productId: number | null = null;
  message: string | null = null;
  submitted = false;

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.isEditMode = !!this.productId;
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoriesService.getCategories().subscribe(categories => {
      this.categories = categories;

      if (this.isEditMode && this.productId) {
        this.loadProduct(this.productId);
      }
    });
  }

  loadProduct(id: number): void {
    this.productsService.getProductById(id).subscribe(product => {
      this.productForm.patchValue({
        ...product,
        idProducto: String(product.idProducto),
        idCategoria: Number(product.idCategoria)
      });
    });
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      this.submitted = true;
      const formValue = {
        idCategoria: Number(this.productForm.value.idCategoria!),
        nombre: this.productForm.value.nombre!,
        precio: Number(this.productForm.value.precio!),
        stock: Number(this.productForm.value.stock!)
      };

      if (this.isEditMode && this.productId) {
        this.productsService.updateProduct(this.productId, formValue).subscribe(() => {
          this.message = 'Se actualizó el producto';
          setTimeout(() => this.router.navigate(['/products']), 2000);
        });
      } else {
        this.productsService.createProduct(formValue).subscribe(product => {
          this.message = `Se registró el producto con el id ${product.idProducto}`;
          this.productForm.get('idProducto')?.setValue(String(product.idProducto));
          setTimeout(() => {
            this.router.navigate(['/products']);
          }, 5000);
        });
      }
    }
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }
}
