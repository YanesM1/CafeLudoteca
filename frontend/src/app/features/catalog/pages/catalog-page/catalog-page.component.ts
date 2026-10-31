import { Component } from '@angular/core';
import { Router } from '@angular/router';

import {
    GameCatalogComponent,
    GameCatalogItem
} from '../../../../shared/organisms/game-catalog/game-catalog.component';

@Component({
    selector: 'app-catalog-page',
    standalone: true,
    imports: [GameCatalogComponent],
    templateUrl: './catalog-page.component.html',
    styleUrl: './catalog-page.component.css'
})
export class CatalogPageComponent {

    games: GameCatalogItem[] = [];
    loading = false;
    selectedGameIds: string[] = [];

    constructor(private router: Router) {}

    onGameDetails(numSerie: string): void {
        console.info('Consultar juego:', numSerie);
    }

    onPreselectionChange(ids: string[]): void {
        this.selectedGameIds = ids;
    }

    onContinueReservation(ids: string[]): void {
        this.router.navigate(['/reservas'], {
            state: { selectedGameIds: ids }
        });
    }
}
