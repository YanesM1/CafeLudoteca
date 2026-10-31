import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import {
    NavbarComponent,
    NavbarLink
} from '../../shared/organisms/navbar/navbar.component';

@Component({
    selector: 'app-admin-layout',
    standalone: true,
    imports: [
        RouterOutlet,
        NavbarComponent
    ],
    templateUrl: './admin-layout.component.html',
    styleUrl: './admin-layout.component.css'
})
export class AdminLayoutComponent {

    brand = 'Café & Ludoteca';
    userLabel = 'Administrador';

    navigationLinks: NavbarLink[] = [
        {
            label: 'Socios',
            route: '/admin/socios',
            icon: 'users',
            exact: true
        },
        {
            label: 'Membresías',
            route: '/admin/membresias',
            icon: 'credit-card',
            exact: true
        }
    ];
}
