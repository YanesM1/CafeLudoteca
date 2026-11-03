import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {
  NavbarComponent,
  NavbarLink
} from '../../shared/organisms/navbar/navbar.component';

@Component({
  selector: 'app-employee-layout',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  templateUrl: './employee-layout.component.html',
  styleUrl: './employee-layout.component.css'
})
export class EmployeeLayoutComponent {
  brand = 'Café & Ludoteca';
  userLabel = 'Empleado';

  navigationLinks: NavbarLink[] = [
    { label: 'Catálogo', route: '/empleado/catalogo', icon: 'dice', exact: true },
    { label: 'Reservas', route: '/empleado/reservas', icon: 'calendar', exact: true },
    { label: 'Préstamos', route: '/empleado/prestamos', icon: 'clipboard-check', exact: true },
    { label: 'Auditoría', route: '/empleado/auditoria', icon: 'clipboard-list', exact: true },
    { label: 'Cambiar rol', route: '/', icon: 'repeat', exact: true }
  ];
}
