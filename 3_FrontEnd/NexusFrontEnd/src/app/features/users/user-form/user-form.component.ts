import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { UsersService } from '../../../core/services/users.service';

@Component({
    selector: 'app-user-form',
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatButtonModule,
        MatCardModule
    ],
    templateUrl: './user-form.component.html',
    styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private usersService = inject(UsersService);

  userForm = this.fb.group({
    idUsuario: [{ value: '', disabled: true }],
    username: ['', Validators.required],
    password: ['', Validators.required],
    estado: [true, Validators.required]
  });

  isEditMode = false;
  private userId: number | null = null;
  message: string | null = null;
  submitted = false;

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.userId) {
      this.isEditMode = true;
      this.usersService.getUserById(this.userId).subscribe(user => {
        this.userForm.patchValue({
          ...user,
          idUsuario: String(user.idUsuario),
          password: '',
          estado: user.estado
        });
      });
    }
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      this.submitted = true;
      const formValue = {
        username: this.userForm.value.username!,
        password: this.userForm.value.password!,
        estado: this.userForm.value.estado!
      };

      if (this.isEditMode && this.userId) {
        this.usersService.updateUser(this.userId, formValue).subscribe(() => {
          this.message = 'Se actualizó el usuario';
          setTimeout(() => this.router.navigate(['/users']), 2000);
        });
      } else {
        this.usersService.createUser(formValue).subscribe(user => {
          this.message = `Se registró el usuario con el id ${user.idUsuario}`;
          this.userForm.get('idUsuario')?.setValue(String(user.idUsuario));
          setTimeout(() => {
            this.router.navigate(['/users']);
          }, 5000);
        });
      }
    }
  }

  goBack(): void {
    this.router.navigate(['/users']);
  }

}
