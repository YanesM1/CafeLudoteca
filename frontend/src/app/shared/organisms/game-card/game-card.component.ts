import { Component, EventEmitter, Input, Output } from '@angular/core';

import { BadgeComponent } from '../../atoms/badge/badge.component';
import { ButtonComponent } from '../../atoms/button/button.component';
import { IconComponent } from '../../atoms/icon/icon.component';

@Component({
    selector: 'app-game-card',
    standalone: true,
    imports: [
        BadgeComponent,
        ButtonComponent,
        IconComponent
    ],
    templateUrl: './game-card.component.html',
    styleUrl: './game-card.component.css'
})
export class GameCardComponent {

    @Input() numSerie: string = '';

    @Input() nombre: string = '';

    @Input() categoria: string = '';

    @Input() imageUrl: string = '';

    @Input() cantidadMinimaJugador: number = 1;

    @Input() cantidadMaximaJugador: number = 4;

    @Input() edadMinima: number = 0;

    @Input() duracionPartida: number = 30;

    @Input() unidadesDisponibles: number = 0;

    @Input() selected: boolean = false;

    @Output() details = new EventEmitter<string>();

    @Output() selectionChange = new EventEmitter<{
        numSerie: string;
        selected: boolean;
    }>();

    verDetalles(): void {
        this.details.emit(this.numSerie);
    }

    cambiarSeleccion(): void {
        if (this.unidadesDisponibles > 0 || this.selected) {
            this.selectionChange.emit({
                numSerie: this.numSerie,
                selected: !this.selected
            });
        }
    }
}
