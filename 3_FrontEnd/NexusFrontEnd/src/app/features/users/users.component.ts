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

import { UsersService } from '../../core/services/users.service';
import { User } from '../../core/models/user.model';

@Component({
    selector: 'app-users',
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
    templateUrl: './users.component.html',
    styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit, AfterViewInit {

  private usersService = inject(UsersService);
  private router = inject(Router);

  displayedColumns: string[] = ['idUsuario', 'username', 'estado', 'acciones'];
  dataSource = new MatTableDataSource<User>();
  message: string | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    this.loadUsers();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadUsers(): void {
    this.usersService.getUsers().subscribe(users => {
      this.dataSource.data = users;
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  newUser(): void {
    this.router.navigate(['/users/new']);
  }

  editUser(id: number): void {
    this.router.navigate(['/users/edit', id]);
  }

  deleteUser(id: number): void {
    const confirmed = window.confirm('¿Está seguro de eliminar este usuario?');

    if (!confirmed) {
      return;
    }

    this.usersService.deleteUser(id).subscribe({
      next: () => {
        this.message = 'Usuario eliminado correctamente.';
        this.loadUsers();
        setTimeout(() => this.message = null, 3000);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          this.message = 'No se encontró el usuario que se intentó eliminar.';
        } else if (error.status === 409) {
          this.message = 'No se puede eliminar el usuario porque tiene ingresos registrados a su nombre.';
        } else {
          this.message = 'Ocurrió un error al eliminar el usuario.';
        }

        setTimeout(() => this.message = null, 3000);
      }
    });
  }

}
