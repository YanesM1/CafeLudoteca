import { Routes } from '@angular/router';

import { ClientLayoutComponent } from './layouts/client-layout/client-layout.component';
import { EmployeeLayoutComponent } from './layouts/employee-layout/employee-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';

import { HomePageComponent } from './features/home/pages/home-page/home-page.component';
import { CatalogPageComponent } from './features/catalog/pages/catalog-page/catalog-page.component';
import { ReservationsPageComponent } from './features/reservations/pages/reservations-page/reservations-page.component';

import { ClientSectionPageComponent } from './features/client-sections/pages/client-section-page/client-section-page.component';

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

            {
                path: 'negociaciones',
                component: ClientSectionPageComponent,
                data: {
                    section: 'negociaciones',
                    title: 'Hablemos de tu próxima experiencia',
                    eyebrow: 'PROPUESTAS Y EXPERIENCIAS',
                    description: 'Comparte tus ideas y propuestas con Café & Ludoteca.'
                }
            },

            {
                path: 'contacto',
                component: ClientSectionPageComponent,
                data: {
                    section: 'contacto',
                    title: 'Estamos para escucharte',
                    eyebrow: 'CONTACTO',
                    description: 'Queremos conocer tus preguntas, comentarios e ideas.'
                }
            },

            {
                path: 'carta',
                component: ClientSectionPageComponent,
                data: {
                    section: 'carta',
                    title: 'Nuestra carta',
                    eyebrow: 'CAFÉ & LUDECA',
                    description: 'Descubre próximamente las opciones disponibles en nuestro espacio.'
                }
            }

        ]
    },

    {
        path: 'empleado',
        component: EmployeeLayoutComponent,
        children: [
            { path: '', redirectTo: 'prestamos', pathMatch: 'full' },
            { path: 'catalogo', component: CatalogPageComponent },
            { path: 'reservas', component: ReservationsPageComponent },
            { path: 'prestamos', component: LoansPageComponent },
            { path: 'auditoria', component: LoansPageComponent }
        ]
    },

    {
        path: 'admin',
        component: AdminLayoutComponent,
        children: [
            { path: '', redirectTo: 'clientes', pathMatch: 'full' },
            { path: 'clientes', component: ClientsPageComponent },
            { path: 'empleados', component: EmployeesPageComponent },
            { path: 'socios', component: SociosPageComponent },
            { path: 'membresias', component: MembershipsPageComponent },
            { path: 'relaciones/beneficios', component: RelationshipAdminPageComponent, data: { kind: 'beneficios' } },
            { path: 'relaciones/clientes-frecuentes', component: RelationshipAdminPageComponent, data: { kind: 'frecuentes' } },
            { path: 'relaciones/telefonos', component: RelationshipAdminPageComponent, data: { kind: 'telefonos' } },
            { path: 'inventario/juegos', component: GamesPageComponent },
            { path: 'inventario/categorias', component: CategoriesPageComponent },
            { path: 'inventario/expansiones', component: ExpansionsPageComponent },
            { path: 'inventario/elementos', component: ElementsPageComponent },
            { path: 'inventario/unidades', component: UnitsPageComponent },
            { path: 'operacion/mesas', component: TablesPageComponent }
        ]
    },

    {
        path: '**',
        redirectTo: '/inicio'
    }

];
