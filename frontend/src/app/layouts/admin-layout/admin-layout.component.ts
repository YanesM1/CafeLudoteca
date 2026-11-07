import { Component, OnDestroy } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Subscription, filter } from 'rxjs';
import {
  NavbarComponent,
  NavbarLink
} from '../../shared/organisms/navbar/navbar.component';

interface AdminSection {
  label: string;
  route: string;
  pages: { label: string; route: string }[];
}

/**
 * Menú de administración por tema: cada opción de la barra superior agrupa
 * sus páginas relacionadas, que aparecen como pestañas debajo.
 */
export const ADMIN_SECTIONS: AdminSection[] = [
  {
    label: 'Clientes',
    route: '/admin/clientes',
    pages: [
      { label: 'Clientes', route: '/admin/clientes/listado' },
      { label: 'Clientes frecuentes', route: '/admin/clientes/frecuentes' },
      { label: 'Socios', route: '/admin/clientes/socios' },
      { label: 'Teléfonos', route: '/admin/clientes/telefonos' }
    ]
  },
  {
    label: 'Membresías',
    route: '/admin/membresias',
    pages: [
      { label: 'Membresías', route: '/admin/membresias/listado' },
      { label: 'Beneficios', route: '/admin/membresias/beneficios' }
    ]
  },
  {
    label: 'Juegos',
    route: '/admin/juegos',
    pages: [
      { label: 'Juegos', route: '/admin/juegos/catalogo' },
      { label: 'Unidades', route: '/admin/juegos/unidades' },
      { label: 'Elementos', route: '/admin/juegos/elementos' },
      { label: 'Expansiones', route: '/admin/juegos/expansiones' },
      { label: 'Categorías', route: '/admin/juegos/categorias' }
    ]
  },
  { label: 'Mesas', route: '/admin/mesas', pages: [] },
  { label: 'Empleados', route: '/admin/empleados', pages: [] }
];

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NavbarComponent],
  templateUrl: './admin-layout.component.html',
  styleUrl: './admin-layout.component.css'
})
export class AdminLayoutComponent implements OnDestroy {
  brand = 'Café & Ludoteca';
  userLabel = 'Administrador';

  readonly navigationLinks: NavbarLink[] = ADMIN_SECTIONS.map(s => ({
    label: s.label,
    route: s.route,
    exact: false
  }));

  activeSection: AdminSection | null = null;

  private readonly sub: Subscription;

  constructor(private router: Router) {
    this.updateSection(this.router.url);
    this.sub = this.router.events
      .pipe(filter((e): e is NavigationEnd => e instanceof NavigationEnd))
      .subscribe(e => this.updateSection(e.urlAfterRedirects));
  }

  private updateSection(url: string): void {
    this.activeSection = ADMIN_SECTIONS.find(s => url.startsWith(s.route)) ?? null;
    // En pantallas angostas las pestañas se desplazan: se deja visible la activa
    setTimeout(() => document.querySelector('.admin-tabs__tab--active')
      ?.scrollIntoView({ block: 'nearest', inline: 'center' }));
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
