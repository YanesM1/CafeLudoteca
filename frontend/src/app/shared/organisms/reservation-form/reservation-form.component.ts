import { Component, EventEmitter, Input, Output } from '@angular/core';

import {
    SelectComponent,
    SelectOption
} from '../../atoms/select/select.component';

import { InputComponent } from '../../atoms/input/input.component';
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
        InputComponent,
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

    @Input() date = '';
    @Input() time = '';
    @Input() minDate = '';

    @Input() selectedTableId = '';

    @Input() tables: ReservationTableOption[] = [];
    @Input() games: ReservationGameItem[] = [];

    @Input() submitting = false;
    @Input() loading = false;
    @Input() errorMessage = '';

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

    private quantities: Record<string, number> = {};

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
            && !this.errorMessage;
    }

    get totalUnits(): number {
        return this.games.reduce(
            (total, game) => total + this.quantityFor(game),
            0
        );
    }

    get actions(): ActionButton[] {
        return [
            {
                id: 'cancel',
                label: 'Cancelar',
                variant: 'danger',
                icon: 'close'
            },
            {
                id: 'confirm',
                label: this.submitting
                    ? 'Procesando...'
                    : 'Confirmar reserva',
                variant: 'gold',
                icon: 'check',
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

    quantityFor(game: ReservationGameItem): number {

        if (game.unidadesDisponibles <= 0) {
            return 0;
        }

        const quantity = this.quantities[game.numSerie] ?? game.cantidad;

        return Math.max(
            1,
            Math.min(quantity, game.unidadesDisponibles)
        );
    }

    quantityValue(game: ReservationGameItem): string {
        return String(this.quantityFor(game));
    }

    maxQuantity(game: ReservationGameItem): string {
        return String(game.unidadesDisponibles);
    }

    onQuantityChange(
        game: ReservationGameItem,
        value: string
    ): void {

        if (value.trim() === '') {
            return;
        }

        const quantity = Number(value);

        if (
            !Number.isInteger(quantity) ||
            game.unidadesDisponibles <= 0
        ) {
            return;
        }

        const validQuantity = Math.max(
            1,
            Math.min(quantity, game.unidadesDisponibles)
        );

        this.quantities = {
            ...this.quantities,
            [game.numSerie]: validQuantity
        };

        this.gameQuantityChange.emit({
            numSerie: game.numSerie,
            cantidad: validQuantity
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
                juegos: this.games.map(game => ({
                    numSerie: game.numSerie,
                    cantidad: this.quantityFor(game)
                }))
            });
        }
    }
}
