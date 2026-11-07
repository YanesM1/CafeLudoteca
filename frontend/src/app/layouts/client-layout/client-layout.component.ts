import { Component, EventEmitter, Input, Output } from '@angular/core';

import { RouterOutlet } from '@angular/router';

import {
    NavbarComponent,
    NavbarLink,
    NavbarAction
} from '../../shared/organisms/navbar/navbar.component';

@Component({
    selector: 'app-client-layout',
    standalone: true,
    imports: [
        RouterOutlet,
        NavbarComponent
    ],
    templateUrl: './client-layout.component.html',
    styleUrl: './client-layout.component.css'
})
export class ClientLayoutComponent {

    @Input() brand = 'Café & Ludoteca';

    @Output() navbarAction = new EventEmitter<NavbarAction>();

    navigationLinks: NavbarLink[] = [

        {
            label: 'Inicio',
            route: '/inicio',
            icon: 'home',
            exact: true
        },

        {
            label: 'Catálogo',
            route: '/catalogo',
            icon: 'dice',
            exact: true
        },

        {
            label: 'Reservas',
            route: '/reservas',
            icon: 'calendar',
            exact: true
        }
    ];

    onNavbarAction(action: NavbarAction): void {
        this.navbarAction.emit(action);
    }
}
