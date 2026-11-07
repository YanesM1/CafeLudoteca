import { Routes } from '@angular/router';

import { ClientLayoutComponent } from './layouts/client-layout/client-layout.component';
import { EmployeeLayoutComponent } from './layouts/employee-layout/employee-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';

import { HomePageComponent } from './features/home/pages/home-page/home-page.component';
import { CatalogPageComponent } from './features/catalog/pages/catalog-page/catalog-page.component';
import { ReservationsPageComponent } from './features/reservations/pages/reservations-page/reservations-page.component';

import { LoansPageComponent } from './features/loans/pages/loans-page/loans-page.component';
import { MembershipsPageComponent } from './features/memberships/pages/memberships-page/memberships-page.component';
import { ClientsPageComponent } from './features/clients/pages/clients-page/clients-page.component';
import { EmployeesPageComponent } from './features/employees/pages/employees-page/employees-page.component';
import { SociosPageComponent } from './features/socios/pages/socios-page/socios-page.component';
import { GamesPageComponent } from './features/inventory/pages/games-page/games-page.component';
import { ExpansionsPageComponent } from './features/inventory/pages/expansions-page/expansions-page.component';
import { ElementsPageComponent } from './features/inventory/pages/elements-page/elements-page.component';
import { UnitsPageComponent } from './features/inventory/pages/units-page/units-page.component';
import { TablesPageComponent } from './features/inventory/pages/tables-page/tables-page.component';
import { CategoriesPageComponent } from './features/inventory/pages/categories-page/categories-page.component';

import { RoleSelectionPageComponent } from './features/role-selection/pages/role-selection-page/role-selection-page.component';

import { RelationshipAdminPageComponent } from './features/relationships/pages/relationship-admin-page/relationship-admin-page.component';
export const routes: Routes = [
    { path: '', component: RoleSelectionPageComponent, pathMatch: 'full' },

    {
        path: '',
        component: ClientLayoutComponent,
        children: [

            { path: '', redirectTo: 'inicio', pathMatch: 'full' },

            { path: 'inicio', component: HomePageComponent },

            { path: 'catalogo', component: CatalogPageComponent },

            { path: 'reservas', component: ReservationsPageComponent },

            // Secciones retiradas: redirigen al inicio
            { path: 'negociaciones', redirectTo: 'inicio', pathMatch: 'full' },
            { path: 'contacto', redirectTo: 'inicio', pathMatch: 'full' },
            { path: 'carta', redirectTo: 'inicio', pathMatch: 'full' }
        ]
    },

    {
        path: 'empleado',
        component: EmployeeLayoutComponent,
        children: [
            { path: '', redirectTo: 'prestamos', pathMatch: 'full' },
            { path: 'catalogo', component: CatalogPageComponent },
            { path: 'reservas', component: ReservationsPageComponent, data: { modo: 'empleado' } },
            { path: 'prestamos', component: LoansPageComponent },
            // Auditoría quedó dentro de Préstamos (historial y devoluciones)
            { path: 'auditoria', redirectTo: 'prestamos', pathMatch: 'full' }
        ]
    },

    {
        path: 'admin',
        component: AdminLayoutComponent,
        children: [
            { path: '', redirectTo: 'clientes/listado', pathMatch: 'full' },

            // Clientes: clientes, frecuentes, socios y teléfonos
            { path: 'clientes', redirectTo: 'clientes/listado', pathMatch: 'full' },
            { path: 'clientes/listado', component: ClientsPageComponent },
            { path: 'clientes/frecuentes', component: RelationshipAdminPageComponent, data: { kind: 'frecuentes' } },
            { path: 'clientes/socios', component: SociosPageComponent },
            { path: 'clientes/telefonos', component: RelationshipAdminPageComponent, data: { kind: 'telefonos' } },

            // Membresías y sus beneficios
            { path: 'membresias', redirectTo: 'membresias/listado', pathMatch: 'full' },
            { path: 'membresias/listado', component: MembershipsPageComponent },
            { path: 'membresias/beneficios', component: RelationshipAdminPageComponent, data: { kind: 'beneficios' } },

            // Juegos: todo lo del inventario
            { path: 'juegos', redirectTo: 'juegos/catalogo', pathMatch: 'full' },
            { path: 'juegos/catalogo', component: GamesPageComponent },
            { path: 'juegos/unidades', component: UnitsPageComponent },
            { path: 'juegos/elementos', component: ElementsPageComponent },
            { path: 'juegos/expansiones', component: ExpansionsPageComponent },
            { path: 'juegos/categorias', component: CategoriesPageComponent },

            { path: 'mesas', component: TablesPageComponent },
            { path: 'empleados', component: EmployeesPageComponent },

            // Direcciones antiguas (por si alguien las tiene guardadas)
            { path: 'personas/clientes', redirectTo: 'clientes/listado', pathMatch: 'full' },
            { path: 'personas/clientes-frecuentes', redirectTo: 'clientes/frecuentes', pathMatch: 'full' },
            { path: 'personas/telefonos', redirectTo: 'clientes/telefonos', pathMatch: 'full' },
            { path: 'personas/empleados', redirectTo: 'empleados', pathMatch: 'full' },
            { path: 'socios/listado', redirectTo: 'clientes/socios', pathMatch: 'full' },
            { path: 'socios/membresias', redirectTo: 'membresias/listado', pathMatch: 'full' },
            { path: 'socios/beneficios', redirectTo: 'membresias/beneficios', pathMatch: 'full' },
            { path: 'inventario/juegos', redirectTo: 'juegos/catalogo', pathMatch: 'full' },
            { path: 'inventario/unidades', redirectTo: 'juegos/unidades', pathMatch: 'full' },
            { path: 'inventario/categorias', redirectTo: 'juegos/categorias', pathMatch: 'full' },
            { path: 'inventario/expansiones', redirectTo: 'juegos/expansiones', pathMatch: 'full' },
            { path: 'inventario/elementos', redirectTo: 'juegos/elementos', pathMatch: 'full' },
            { path: 'operacion/mesas', redirectTo: 'mesas', pathMatch: 'full' },
            { path: 'relaciones/beneficios', redirectTo: 'membresias/beneficios', pathMatch: 'full' },
            { path: 'relaciones/clientes-frecuentes', redirectTo: 'clientes/frecuentes', pathMatch: 'full' },
            { path: 'relaciones/telefonos', redirectTo: 'clientes/telefonos', pathMatch: 'full' }
        ]
    },

    {
        path: '**',
        redirectTo: '/inicio'
    }

];
