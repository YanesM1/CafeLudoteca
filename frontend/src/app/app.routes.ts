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

export const routes: Routes = [

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
            { path: 'prestamos', component: LoansPageComponent },
            { path: 'auditoria', component: LoansPageComponent }
        ]
    },

    {
        path: 'admin',
        component: AdminLayoutComponent,
        children: [
            { path: '', redirectTo: 'membresias', pathMatch: 'full' },
            { path: 'clientes', component: ClientsPageComponent },
            { path: 'empleados', component: EmployeesPageComponent },
            { path: 'socios', component: MembershipsPageComponent },
            { path: 'membresias', component: MembershipsPageComponent }
        ]
    },

    {
        path: '**',
        redirectTo: '/inicio'
    }

];
