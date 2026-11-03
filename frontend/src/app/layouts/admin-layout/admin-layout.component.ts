import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {
  NavbarComponent,
  NavbarLink
} from '../../shared/organisms/navbar/navbar.component';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  templateUrl: './admin-layout.component.html',
  styleUrl: './admin-layout.component.css'
})
export class AdminLayoutComponent {
  brand = 'Café & Ludoteca';
  userLabel = 'Administrador';

  navigationLinks: NavbarLink[] = [
    {
      label: 'Clientes',
      route: '/admin/clientes',
      icon: 'users',
      exact: true
    },
    {
      label: 'Empleados',
      route: '/admin/empleados',
      icon: 'users',
      exact: true
    },
    {
      label: 'Socios',
      route: '/admin/socios',
      icon: 'user-round',
      exact: true
    },
    {
      label: 'Membresías',
      route: '/admin/membresias',
      icon: 'credit-card',
      exact: true
    },
    {
      label: 'Juegos e inventario',
      route: '/admin/inventario/juegos',
      icon: 'dice',
      exact: true
    },
    {
      label: 'Categorías',
      route: '/admin/inventario/categorias',
      icon: 'tags',
      exact: true
    },
    {
      label: 'Expansiones',
      route: '/admin/inventario/expansiones',
      icon: 'package',
      exact: true
    },
    {
      label: 'Elementos de juego',
      route: '/admin/inventario/elementos',
      icon: 'puzzle',
      exact: true
    },
    {
      label: 'Unidades físicas',
      route: '/admin/inventario/unidades',
      icon: 'boxes',
      exact: true
    },
    {
      label: 'Mesas',
      route: '/admin/operacion/mesas',
      icon: 'table',
      exact: true
    },
    {
      label: 'Beneficios',
      route: '/admin/relaciones/beneficios',
      icon: 'gift',
      exact: true
    },
    {
      label: 'Clientes frecuentes',
      route: '/admin/relaciones/clientes-frecuentes',
      icon: 'star',
      exact: true
    },
    {
      label: 'Teléfonos',
      route: '/admin/relaciones/telefonos',
      icon: 'phone',
      exact: true
    },
    { label: 'Cambiar rol', route: '/', icon: 'repeat', exact: true },
  ];
}
