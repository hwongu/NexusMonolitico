
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { CategoriesService } from '../../../core/services/categories.service';
import { Category } from '../../../core/models/category.model';
import { timeInterval, timeout } from 'rxjs';

@Component({
    selector: 'app-category-form',
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatCardModule
    ],
    templateUrl: './category-form.component.html',
    styleUrls: ['./category-form.component.css']
})
export class CategoryFormComponent implements OnInit {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private categoriesService = inject(CategoriesService);

  categoryForm = this.fb.group({
    idCategoria: [{ value: '', disabled: true }],
    nombre: ['', Validators.required],
    descripcion: ['', Validators.required]
  });

  isEditMode = false;
  private categoryId: number | null = null;
  message: string | null = null;
  submitted = false;

  ngOnInit(): void {
    this.categoryId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.categoryId) {
      this.isEditMode = true;
      this.categoriesService.getCategoryById(this.categoryId).subscribe(category => {
        this.categoryForm.patchValue({
          ...category,
          idCategoria: String(category.idCategoria)
        });
      });
    }
  }

  onSubmit(): void {
    if (this.categoryForm.valid) {
      this.submitted = true;
      const formValue = {
        nombre: this.categoryForm.value.nombre!,
        descripcion: this.categoryForm.value.descripcion!
      };

      if (this.isEditMode && this.categoryId) {
        this.categoriesService.updateCategory(this.categoryId, formValue).subscribe(() => {
          this.message = 'Se actualizo la categoria';
          setTimeout(() => this.router.navigate(['/categories']), 2000);
        });
      } else {
        this.categoriesService.createCategory(formValue).subscribe(category => {
          this.message = `Se registró la categoría con el id ${category.idCategoria}`;
          this.categoryForm.get('idCategoria')?.setValue(String(category.idCategoria));
          setTimeout(() => {
            this.router.navigate(['/categories']);
          }, 5000);
        });
      }
    }
  }

  goBack(): void {
    this.router.navigate(['/categories']);
  }
}
