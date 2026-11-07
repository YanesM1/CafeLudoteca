import { Component, EventEmitter, Input, Output } from '@angular/core';

import {
    SelectComponent,
    SelectOption
} from '../../atoms/select/select.component';

import { LabelComponent } from '../../atoms/label/label.component';
import { BadgeComponent } from '../../atoms/badge/badge.component';
import { IconComponent } from '../../atoms/icon/icon.component';

import { DateTimePickerComponent } from '../../molecules/date-time-picker/date-time-picker.component';

import { LoadingStateComponent } from '../../molecules/loading-state/loading-state.component';

import {
    ActionButtonsComponent,
    ActionButton
} from '../../molecules/action-buttons/action-buttons.component';

export interface ReservationTableOption {
    idMesa: string;
    label: string;
    capacidad: number;
    tematica?: string;
    ubicacion?: string;
    disponible?: boolean;
}

export interface ReservationGameItem {
    numSerie: string;
    nombre: string;
    unidadesDisponibles: number;
    cantidad: number;
}

export interface ReservationPayload {
    fecha: string;
    horario: string;
    idMesa: string;
    juegos: {
        numSerie: string;
        cantidad: number;
    }[];
}

@Component({
    selector: 'app-reservation-form',
    standalone: true,
    imports: [
        SelectComponent,
        LabelComponent,
        BadgeComponent,
        IconComponent,
        DateTimePickerComponent,
        LoadingStateComponent,
        ActionButtonsComponent
    ],
    templateUrl: './reservation-form.component.html',
    styleUrl: './reservation-form.component.css'
})
export class ReservationFormComponent {
    /** Paso 1: datos del cliente (el campo llega por proyección de contenido). */
    @Input() clientStepTitle = 'Tus datos';
    @Input() clientStepHint = 'Escribe tu número de documento.';


    @Input() date = '';
    @Input() time = '';
    @Input() minDate = '';

    @Input() selectedTableId = '';

    @Input() tables: ReservationTableOption[] = [];
    @Input() games: ReservationGameItem[] = [];

    @Input() submitting = false;
    @Input() loading = false;
    @Input() errorMessage = '';
    @Input() submitError = '';

    @Output() dateChange = new EventEmitter<string>();
    @Output() timeChange = new EventEmitter<string>();
    @Output() tableChange = new EventEmitter<string>();

    @Output() gameQuantityChange = new EventEmitter<{
        numSerie: string;
        cantidad: number;
    }>();

    @Output() confirm = new EventEmitter<ReservationPayload>();
    @Output() cancel = new EventEmitter<void>();
    @Output() retry = new EventEmitter<void>();

    get selectedCount(): number {
        return this.games.filter(game => this.isSelected(game)).length;
    }

    get tableOptions(): SelectOption[] {
        return this.tables.map(table => ({
            value: table.idMesa,
            label: `${table.label} - ${table.capacidad} personas`,
            disabled: table.disponible === false
        }));
    }

    get selectedTable(): ReservationTableOption | undefined {
        return this.tables.find(
            table => table.idMesa === this.selectedTableId
        );
    }

    get canSubmit(): boolean {
        return !!this.date
            && !!this.time
            && !!this.selectedTable
            && this.selectedTable.disponible !== false
            && !this.submitting
            && !this.loading
            && this.selectedCount > 0;
    }

    get actions(): ActionButton[] {
        return [
            {
                id: 'cancel',
                label: 'Cancelar',
                variant: 'ghost'
            },
            {
                id: 'confirm',
                label: this.submitting
                    ? 'Procesando...'
                    : 'Confirmar reserva',
                variant: 'gold',
                disabled: !this.canSubmit
            }
        ];
    }

    onDateChange(value: string): void {
        this.date = value;
        this.dateChange.emit(value);
    }

    onTimeChange(value: string): void {
        this.time = value;
        this.timeChange.emit(value);
    }

    onTableChange(value: string): void {
        this.selectedTableId = value;
        this.tableChange.emit(value);
    }

    /** Juegos elegidos (llegan del catálogo o se agregan aquí). */
    get selectedGames(): ReservationGameItem[] {
        return this.games.filter(game => this.isSelected(game));
    }

    /** Juegos que se pueden agregar: con unidades disponibles y aún no elegidos. */
    get availableGames(): ReservationGameItem[] {
        return this.games
            .filter(game => !this.isSelected(game) && game.unidadesDisponibles > 0)
            .sort((a, b) => a.nombre.localeCompare(b.nombre));
    }

    agregarJuego(numSerie: string): void {
        const game = this.games.find(g => g.numSerie === numSerie);
        if (game && !this.isSelected(game)) this.toggleGame(game);
    }

    isSelected(game: ReservationGameItem): boolean {
        return game.cantidad > 0;
    }

    // Un juego se incluye (1) o no (0) en la reserva: el backend guarda
    // la lista de juegos preseleccionados, no cantidades.
    toggleGame(game: ReservationGameItem): void {
        this.gameQuantityChange.emit({
            numSerie: game.numSerie,
            cantidad: this.isSelected(game) ? 0 : 1
        });
    }

    onRetry(): void {
        this.retry.emit();
    }

    onActionSelected(action: string): void {

        if (action === 'cancel') {
            this.cancel.emit();
        }

        if (action === 'confirm' && this.canSubmit) {

            this.confirm.emit({
                fecha: this.date,
                horario: this.time,
                idMesa: this.selectedTableId,
                juegos: this.games
                    .filter(game => this.isSelected(game))
                    .map(game => ({ numSerie: game.numSerie, cantidad: 1 }))
            });
        }
    }
}
