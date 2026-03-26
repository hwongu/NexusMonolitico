import { Component, OnInit, inject, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

import { RevenuesService } from '../../core/services/revenues.service';
import { Revenue, RevenueDetail } from '../../core/models/revenue.model';
import { UsersService } from '../../core/services/users.service';

@Component({
    selector: 'app-revenues',
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
    templateUrl: './revenues.component.html',
    styleUrls: ['./revenues.component.css']
})
export class RevenuesComponent implements OnInit, AfterViewInit {

  private revenuesService = inject(RevenuesService);
  private usersService = inject(UsersService);
  private router = inject(Router);

  displayedColumns: string[] = ['idIngreso', 'idUsuario', 'fechaIngreso', 'estado', 'acciones'];
  detailColumns: string[] = ['idProducto', 'nombreProducto', 'cantidad', 'precioCompra'];
  dataSource = new MatTableDataSource<Revenue>();
  detailDataSource = new MatTableDataSource<RevenueDetail>();
  message: string | null = null;
  selectedRevenueId: number | null = null;
  userNames: Record<number, string> = {};
  loadingUsers = new Set<number>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    this.loadRevenues();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadRevenues(): void {
    this.revenuesService.getRevenues().subscribe(revenues => {
      this.dataSource.data = revenues;
      this.loadUserNames(revenues);
    });
  }

  loadUserNames(revenues: Revenue[]): void {
    const userIds = [...new Set(revenues.map(revenue => revenue.idUsuario))];

    userIds.forEach(id => {
      if (this.userNames[id] || this.loadingUsers.has(id)) {
        return;
      }

      this.loadingUsers.add(id);
      this.usersService.getUserById(id).subscribe({
        next: user => {
          this.userNames[id] = user.usuario || user.username || `Usuario ${id}`;
          this.loadingUsers.delete(id);
        },
        error: () => {
          this.userNames[id] = `Usuario ${id}`;
          this.loadingUsers.delete(id);
        }
      });
    });
  }

  getUserName(id: number): string {
    return this.userNames[id] || `Usuario ${id}`;
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  newRevenue(): void {
    this.router.navigate(['/revenues/new']);
  }

  viewDetails(id: number): void {
    this.selectedRevenueId = id;
    this.revenuesService.getRevenueDetails(id).subscribe(details => {
      this.detailDataSource.data = details;
    });
  }

  normalizeState(state: string): string {
    return (state || '').trim().toUpperCase();
  }

  isReceived(state: string): boolean {
    return this.normalizeState(state) === 'RECIBIDO';
  }

  completeRevenue(id: number): void {
    const confirmed = window.confirm('¿Desea cambiar el estado del ingreso a COMPLETADO?');

    if (!confirmed) {
      return;
    }

    this.revenuesService.updateRevenueStatus(id, { estado: 'COMPLETADO' }).subscribe(() => {
      this.message = `Se actualizó el estado del ingreso con id ${id}`;
      this.loadRevenues();
      setTimeout(() => this.message = null, 3000);
    });
  }

  deleteRevenue(id: number): void {
    this.revenuesService.deleteRevenue(id).subscribe(() => {
      this.message = `Se anuló el ingreso con id ${id}`;
      this.loadRevenues();

      if (this.selectedRevenueId === id) {
        this.selectedRevenueId = null;
        this.detailDataSource.data = [];
      }

      setTimeout(() => this.message = null, 3000);
    });
  }

}
