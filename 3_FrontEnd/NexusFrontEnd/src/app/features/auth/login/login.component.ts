
import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/auth/auth.service';

/**
 * @author Henry Wong (hwongu@gmail.com)
 */
@Component({
    selector: 'app-login',
    imports: [
        CommonModule,
        FormsModule,
        RouterModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatSnackBarModule
    ],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  username = '';
  password = '';

  login() {
    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        let message = 'Ha ocurrido un error inesperado. Por favor, inténtelo de nuevo.';
        if (err.status === 0 || err.status === 500) {
          message = 'El servicio de inicio de sesión no está disponible en este momento. Por favor, inténtelo de nuevo más tarde.';
        } else if (err.status === 401) {
          message = 'Usuario y/o contraseña incorrecta';
        }
        this.snackBar.open(message, 'Cerrar', {
          duration: 3000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
        console.error(err);
      }
    });
  }
}
