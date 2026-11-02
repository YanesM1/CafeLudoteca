import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import {
    GameCatalogComponent,
    GameCatalogItem
} from '../../../../shared/organisms/game-catalog/game-catalog.component';

import { JuegoMesaService } from '../../../../core/services/juego-mesa.service';
import { JuegoMesaResponse } from '../../../../core/models/juego-mesa.model';

@Component({
    selector: 'app-catalog-page',
    standalone: true,
    imports: [GameCatalogComponent],
    templateUrl: './catalog-page.component.html',
    styleUrl: './catalog-page.component.css'
})
export class CatalogPageComponent implements OnInit {

    games: GameCatalogItem[] = [];

    loading = true;

    errorMessage = '';

    selectedGameIds: string[] = [];

    constructor(
        private readonly router: Router,
        private readonly juegoMesaService: JuegoMesaService
    ) {}

    ngOnInit(): void {
        this.loadGames();
    }

    loadGames(): void {

        this.loading = true;
        this.errorMessage = '';

        this.juegoMesaService.listar().subscribe({

            next: (response: JuegoMesaResponse[]) => {

                this.games = response.map(game => ({
                    numSerie: game.numSerie,
                    nombre: game.nombre,
                    categoria: null,
                    imageUrl: '',
                    cantidadMinimaJugador: game.cantidadMinimaJugadores,
                    cantidadMaximaJugador: game.cantidadMaximaJugadores,
                    edadMinima: game.edadMinima,
                    duracionPartida: game.duracionPartida,
                    unidadesDisponibles: null
                }));

                this.loading = false;
            },

            error: error => {

                console.error('Error al consultar el catálogo:', error);

                this.errorMessage =
                    'No fue posible consultar los juegos. Verifica la conexión con el servidor.';

                this.loading = false;
            }
        });
    }

    onRetry(): void {
        this.loadGames();
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
