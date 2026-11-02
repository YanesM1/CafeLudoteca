import { Component, EventEmitter, Input, Output } from '@angular/core';

import { GameCardComponent } from '../game-card/game-card.component';

import {
    GameDetailsModalComponent,
    GameDetailsData
} from '../game-details-modal/game-details-modal.component';

import { SearchBarComponent } from '../../molecules/search-bar/search-bar.component';

import {
    FilterGroupComponent,
    FilterDefinition
} from '../../molecules/filter-group/filter-group.component';

import { LoadingStateComponent } from '../../molecules/loading-state/loading-state.component';

import { ButtonComponent } from '../../atoms/button/button.component';
import { IconComponent } from '../../atoms/icon/icon.component';

export interface GameCatalogItem extends GameDetailsData {}

@Component({
    selector: 'app-game-catalog',
    standalone: true,
    imports: [
        GameCardComponent,
        GameDetailsModalComponent,
        SearchBarComponent,
        FilterGroupComponent,
        LoadingStateComponent,
        ButtonComponent,
        IconComponent
    ],
    templateUrl: './game-catalog.component.html',
    styleUrl: './game-catalog.component.css'
})
export class GameCatalogComponent {

    @Input() games: GameCatalogItem[] = [];
    @Input() loading = false;
    @Input() errorMessage = '';

    @Output() gameDetails = new EventEmitter<string>();
    @Output() preselectionChange = new EventEmitter<string[]>();
    @Output() continueReservation = new EventEmitter<string[]>();
    @Output() retry = new EventEmitter<void>();

    searchTerm = '';

    filterValues: Record<string, string> = {};

    selectedGameIds = new Set<string>();

    selectedGame: GameCatalogItem | null = null;

    detailsOpen = false;

    get filters(): FilterDefinition[] {

        const categories = [
            ...new Set(
                this.games
                    .map(game => game.categoria ?? '')
                    .filter(category => category.trim() !== '')
            )
        ].sort();

        const definitions: FilterDefinition[] = [];

        if (categories.length > 0) {
            definitions.push({
                name: 'categoria',
                label: 'Categoría',
                placeholder: 'Todas las categorías',
                options: categories.map(category => ({
                    value: category,
                    label: category
                }))
            });
        }

        definitions.push({
            name: 'jugadores',
            label: 'Jugadores',
            placeholder: 'Cualquier cantidad',
            options: [
                { value: '1', label: '1 jugador' },
                { value: '2', label: '2 jugadores' },
                { value: '3', label: '3 jugadores' },
                { value: '4', label: '4 jugadores' },
                { value: '5', label: '5 jugadores' },
                { value: '6', label: '6 jugadores' }
            ]
        });

        return definitions;
    }

    get filteredGames(): GameCatalogItem[] {

        const term = this.searchTerm.trim().toLowerCase();

        return this.games.filter(game => {

            const category = game.categoria ?? '';

            const matchesSearch =
                game.nombre.toLowerCase().includes(term) ||
                category.toLowerCase().includes(term) ||
                game.numSerie.toLowerCase().includes(term);

            const matchesCategory =
                !this.filterValues['categoria'] ||
                category === this.filterValues['categoria'];

            const players = Number(this.filterValues['jugadores']);

            const matchesPlayers =
                !this.filterValues['jugadores'] ||
                (
                    game.cantidadMinimaJugador <= players &&
                    game.cantidadMaximaJugador >= players
                );

            return matchesSearch && matchesCategory && matchesPlayers;
        });
    }

    get selectedCount(): number {
        return this.selectedGameIds.size;
    }

    onSearch(value: string): void {
        this.searchTerm = value;
    }

    onGameDetails(numSerie: string): void {

        this.selectedGame = this.games.find(
            game => game.numSerie === numSerie
        ) ?? null;

        this.detailsOpen = !!this.selectedGame;

        this.gameDetails.emit(numSerie);
    }

    closeDetails(): void {
        this.detailsOpen = false;
        this.selectedGame = null;
    }

    onSelectionChange(event: {
        numSerie: string;
        selected: boolean;
    }): void {

        const updatedSelection = new Set(this.selectedGameIds);

        if (event.selected) {
            updatedSelection.add(event.numSerie);
        } else {
            updatedSelection.delete(event.numSerie);
        }

        this.selectedGameIds = updatedSelection;

        this.preselectionChange.emit(
            Array.from(this.selectedGameIds)
        );
    }

    onContinueReservation(): void {

        if (this.selectedGameIds.size === 0) {
            return;
        }

        this.continueReservation.emit(
            Array.from(this.selectedGameIds)
        );
    }

    onRetry(): void {
        this.retry.emit();
    }
}
