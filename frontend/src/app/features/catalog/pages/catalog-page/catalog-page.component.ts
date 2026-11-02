import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import {
    GameCatalogComponent,
    GameCatalogItem
} from '../../../../shared/organisms/game-catalog/game-catalog.component';

import { JuegoMesaService } from '../../../../core/services/juego-mesa.service';
import { UnidadJuegoService } from '../../../../core/services/unidad-juego.service';
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
        private readonly juegoMesaService: JuegoMesaService,
        private readonly unidadJuegoService: UnidadJuegoService
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

                this.unidadJuegoService.consultarDisponibilidad().subscribe({
                    next: disponibilidad => {
                        const conteos = new Map<string, number>();

                        disponibilidad.forEach(item => {
                            conteos.set(
                                item.numSerie,
                                item.unidadesDisponibles
                            );
                        });

                        this.games = this.games.map(game => ({
                            ...game,
                            unidadesDisponibles:
                                conteos.get(game.numSerie) ?? 0
                        }));
                    },
                    error: error => {
                        console.error(
                            'Error consultando disponibilidad:',
                            error
                        );
                    }
                });

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
