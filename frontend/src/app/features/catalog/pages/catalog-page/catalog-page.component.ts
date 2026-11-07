import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import {
    GameCatalogComponent,
    GameCatalogItem
} from '../../../../shared/organisms/game-catalog/game-catalog.component';

import { JuegoMesaService } from '../../../../core/services/juego-mesa.service';
import { UnidadJuegoService } from '../../../../core/services/unidad-juego.service';
import { JuegoMesaResponse } from '../../../../core/models/juego-mesa.model';

import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CategoriaService } from '../../../../core/services/categoria.service';
import { mensajeError } from '../../../../core/utils/http-error';

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

    get eyebrow(): string {
        return this.router.url.startsWith('/empleado') ? 'Empleado · Catálogo' : 'Cliente · Catálogo';
    }

    constructor(
        private readonly router: Router,
        private readonly juegoMesaService: JuegoMesaService,
        private readonly unidadJuegoService: UnidadJuegoService,
        private readonly categoriaService: CategoriaService
    ) {}

    ngOnInit(): void {
        this.loadGames();
    }

    loadGames(): void {
        this.loading = true;
        this.errorMessage = '';
        forkJoin({
            juegos: this.juegoMesaService.listar(),
            disponibilidad: this.unidadJuegoService.consultarDisponibilidad()
                .pipe(catchError(() => of([]))),
            categorias: this.categoriaService.listar()
                .pipe(catchError(() => of([])))
        }).subscribe({
            next: ({ juegos, disponibilidad, categorias }) => {
                const conteos = new Map<string, number>();
                disponibilidad.forEach(item =>
                    conteos.set(item.numSerie, item.unidadesDisponibles));

                const categoriaPorJuego = new Map<string, string>();
                categorias.forEach(c => {
                    const actual = categoriaPorJuego.get(c.numSerie);
                    categoriaPorJuego.set(c.numSerie,
                        actual ? `${actual}, ${c.categoria}` : c.categoria);
                });

                this.games = juegos.map((game: JuegoMesaResponse) => ({
                    numSerie: game.numSerie,
                    nombre: game.nombre,
                    categoria: categoriaPorJuego.get(game.numSerie) ?? null,
                    imageUrl: '',
                    cantidadMinimaJugador: game.cantidadMinimaJugadores,
                    cantidadMaximaJugador: game.cantidadMaximaJugadores,
                    edadMinima: game.edadMinima,
                    duracionPartida: game.duracionPartida,
                    unidadesDisponibles: conteos.get(game.numSerie) ?? 0
                }));
                this.loading = false;
            },
            error: error => {
                this.errorMessage = mensajeError(error,
                    'No fue posible consultar los juegos. Verifica la conexión con el servidor.');
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

        // Mantener al usuario en su zona (cliente o empleado)
        const destino = this.router.url.startsWith('/empleado')
            ? '/empleado/reservas'
            : '/reservas';
        this.router.navigate([destino], {
            state: { selectedGameIds: ids }
        });
    }
}
