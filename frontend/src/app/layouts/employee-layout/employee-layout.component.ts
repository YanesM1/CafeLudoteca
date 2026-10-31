import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import {
    NavbarComponent,
    NavbarLink
} from '../../shared/organisms/navbar/navbar.component';

@Component({
    selector: 'app-employee-layout',
    standalone: true,
    imports: [
        RouterOutlet,
        NavbarComponent
    ],
    templateUrl: './employee-layout.component.html',
    styleUrl: './employee-layout.component.css'
})
export class EmployeeLayoutComponent {

    brand = 'Café & Ludoteca';
    userLabel = 'Empleado';

    navigationLinks: NavbarLink[] = [
        {
            label: 'Préstamos',
            route: '/empleado/prestamos',
            icon: 'dice',
            exact: true
        },
        {
            label: 'Auditoría',
            route: '/empleado/auditoria',
            icon: 'clipboard-check',
            exact: true
        }
    ];
}
