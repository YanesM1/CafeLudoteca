import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

export interface NavbarLink {
    label: string;
    route: string;
    icon?: string;
    exact?: boolean;
}

export interface NavbarAction {
    type: 'cart' | 'user';
}

@Component({
    selector: 'app-navbar',
    standalone: true,
    imports: [
        RouterLink,
        RouterLinkActive
    ],
    templateUrl: './navbar.component.html',
    styleUrl: './navbar.component.css'
})
export class NavbarComponent {

    @Input() brand = 'Café & Ludoteca';

    @Input() homeRoute = '/catalogo';

    @Input() links: NavbarLink[] = [];

    @Input() showCartButton = false;

    @Input() showUserButton = true;

    @Input() cartCount = 0;

    @Input() userLabel = 'Usuario';
    @Input() showRoleSwitch = false;

    @Output() actionSelected = new EventEmitter<NavbarAction>();

    menuOpen = false;

    toggleMenu(): void {
        this.menuOpen = !this.menuOpen;
    }

    closeMenu(): void {
        this.menuOpen = false;
    }

    onCartClick(): void {
        this.actionSelected.emit({
            type: 'cart'
        });

        this.closeMenu();
    }

    onUserClick(): void {
        this.actionSelected.emit({
            type: 'user'
        });

        this.closeMenu();
    }
}
