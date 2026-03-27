
import { Component, OnInit, inject } from '@angular/core';
import { AuthService } from '../../core/auth/auth.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';

/**
 * @author Henry Wong (hwongu@gmail.com)
 */
@Component({
    selector: 'app-dashboard',
    imports: [CommonModule, RouterModule, MatCardModule],
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  private authService = inject(AuthService);
  user: any;

  ngOnInit(): void {
    this.user = this.authService.getUser();
  }
}
