import {
    Component,
    EventEmitter,
    Input,
    Output
} from '@angular/core';

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

    @Input() numSerie = '';
    @Input() nombre = '';
    @Input() categoria: string | null = null;
    @Input() imageUrl = '';
    @Input() cantidadMinimaJugador = 1;
    @Input() cantidadMaximaJugador = 4;
    @Input() edadMinima = 0;
    @Input() duracionPartida = 30;
    @Input() unidadesDisponibles: number | null = null;
    @Input() selected = false;

    @Output() details = new EventEmitter<string>();

    @Output() selectionChange = new EventEmitter<{
        numSerie: string;
        selected: boolean;
    }>();

    verDetalles(): void {
        this.details.emit(this.numSerie);
    }

    cambiarSeleccion(): void {

        if (
            (this.unidadesDisponibles !== null &&
                this.unidadesDisponibles > 0) ||
            this.selected
        ) {
            this.selectionChange.emit({
                numSerie: this.numSerie,
                selected: !this.selected
            });
        }
    }
}
