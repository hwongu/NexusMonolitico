import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { ProductsService } from '../../../core/services/products.service';
import { RevenuesService } from '../../../core/services/revenues.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Product } from '../../../core/models/product.model';
import { RevenueCreatePayload, RevenueDetail } from '../../../core/models/revenue.model';

@Component({
    selector: 'app-revenue-form',
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatButtonModule,
        MatCardModule,
        MatTableModule,
        MatIconModule
    ],
    templateUrl: './revenue-form.component.html',
    styleUrls: ['./revenue-form.component.css']
})
export class RevenueFormComponent implements OnInit {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private productsService = inject(ProductsService);
  private revenuesService = inject(RevenuesService);
  private authService = inject(AuthService);

  revenueForm = this.fb.group({
    idUsuario: [{ value: '', disabled: true }],
    fechaIngreso: [{ value: '', disabled: true }],
    estado: [{ value: 'RECIBIDO', disabled: true }],
    idProducto: [null as number | null, Validators.required],
    stockActual: [{ value: '', disabled: true }],
    cantidad: [null as number | null, [Validators.required, Validators.min(1)]],
    precioCompra: [null as number | null, [Validators.required, Validators.min(0.01)]]
  });

  products: Product[] = [];
  details: RevenueDetail[] = [];
  dataSource = new MatTableDataSource<RevenueDetail>();
  displayedColumns: string[] = ['nombreProducto', 'stockActual', 'cantidad', 'precioCompra', 'acciones'];
  message: string | null = null;
  submitted = false;
  currentUserId: number | null = null;

  ngOnInit(): void {
    this.loadProducts();
    this.loadSessionUser();

    this.revenueForm.get('idProducto')?.valueChanges.subscribe(productId => {
      this.updateCurrentStock(productId);
    });
  }

  loadProducts(): void {
    this.productsService.getProducts().subscribe(products => {
      this.products = products;
    });
  }

  loadSessionUser(): void {
    const user = this.authService.getUser();
    const userId = user?.idUsuario ? Number(user.idUsuario) : null;

    this.currentUserId = userId;
    this.revenueForm.patchValue({
      idUsuario: userId ? String(userId) : '',
      fechaIngreso: this.getCurrentDateTime(),
      estado: 'RECIBIDO'
    });
  }

  updateCurrentStock(productId: number | null): void {
    const selectedProduct = this.products.find(product => product.idProducto === Number(productId));
    this.revenueForm.get('stockActual')?.setValue(selectedProduct ? String(selectedProduct.stock) : '');
  }

  addDetail(): void {
    if (
      !this.revenueForm.get('idProducto')?.valid ||
      !this.revenueForm.get('cantidad')?.valid ||
      !this.revenueForm.get('precioCompra')?.valid
    ) {
      return;
    }

    const productId = Number(this.revenueForm.value.idProducto!);
    const selectedProduct = this.products.find(product => product.idProducto === productId);

    if (!selectedProduct) {
      return;
    }

    const detail: RevenueDetail = {
      idProducto: productId,
      nombreProducto: selectedProduct.nombre,
      stockActual: selectedProduct.stock,
      cantidad: Number(this.revenueForm.value.cantidad!),
      precioCompra: Number(this.revenueForm.value.precioCompra!)
    };

    const index = this.details.findIndex(item => item.idProducto === detail.idProducto);

    if (index >= 0) {
      this.details[index] = detail;
    } else {
      this.details.push(detail);
    }

    this.dataSource.data = [...this.details];
    this.revenueForm.patchValue({
      idProducto: null,
      stockActual: '',
      cantidad: null,
      precioCompra: null
    });
  }

  removeDetail(idProducto: number): void {
    this.details = this.details.filter(detail => detail.idProducto !== idProducto);
    this.dataSource.data = [...this.details];
  }

  saveRevenue(): void {
    if (!this.currentUserId || this.details.length === 0) {
      return;
    }

    this.submitted = true;

    const payload: RevenueCreatePayload = {
      ingreso: {
        idUsuario: this.currentUserId,
        fechaIngreso: this.getCurrentDateTime(),
        estado: 'RECIBIDO'
      },
      detalles: this.details.map(detail => ({
        idProducto: Number(detail.idProducto),
        cantidad: Number(detail.cantidad),
        precioCompra: Number(detail.precioCompra)
      }))
    };

    this.revenuesService.createRevenue(payload).subscribe(() => {
      this.message = 'Se registró el ingreso';
      setTimeout(() => {
        this.router.navigate(['/revenues']);
      }, 2000);
    });
  }

  goBack(): void {
    this.router.navigate(['/revenues']);
  }

  private getCurrentDateTime(): string {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  }
}
