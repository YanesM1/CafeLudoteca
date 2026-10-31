import { Component, EventEmitter, Input, Output } from '@angular/core';

import { ButtonComponent } from '../../atoms/button/button.component';
import { BadgeComponent } from '../../atoms/badge/badge.component';
import { IconComponent } from '../../atoms/icon/icon.component';

export interface GameDetailsData {
    numSerie: string;
    nombre: string;
    categoria: string;
    imageUrl?: string;
    cantidadMinimaJugador: number;
    cantidadMaximaJugador: number;
    edadMinima: number;
    duracionPartida: number;
    unidadesDisponibles: number;
}

@Component({
    selector: 'app-game-details-modal',
    standalone: true,
    imports: [
        ButtonComponent,
        BadgeComponent,
        IconComponent
    ],
    templateUrl: './game-details-modal.component.html',
    styleUrl: './game-details-modal.component.css'
})
export class GameDetailsModalComponent {

    @Input() visible = false;
    @Input() game: GameDetailsData | null = null;

    @Output() close = new EventEmitter<void>();

    onClose(): void {
        this.close.emit();
    }
}
