
import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { LayoutComponent } from './core/layout/layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';
import { CategoriesComponent } from './features/categories/categories.component';
import { ProductsComponent } from './features/products/products.component';
import { UsersComponent } from './features/users/users.component';
import { RevenuesComponent } from './features/revenues/revenues.component';
import { CategoryFormComponent } from './features/categories/category-form/category-form.component';
import { UserFormComponent } from './features/users/user-form/user-form.component';
import { ProductFormComponent } from './features/products/product-form/product-form.component';
import { RevenueFormComponent } from './features/revenues/revenue-form/revenue-form.component';

/**
 * @author Henry Wong (hwongu@gmail.com)
 */
export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'categories', component: CategoriesComponent },
      { path: 'categories/new', component: CategoryFormComponent },
      { path: 'categories/edit/:id', component: CategoryFormComponent },
      { path: 'products', component: ProductsComponent },
      { path: 'products/new', component: ProductFormComponent },
      { path: 'products/edit/:id', component: ProductFormComponent },
      { path: 'users', component: UsersComponent },
      { path: 'users/new', component: UserFormComponent },
      { path: 'users/edit/:id', component: UserFormComponent },
      { path: 'revenues', component: RevenuesComponent },
      { path: 'revenues/new', component: RevenueFormComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'login' }
];
