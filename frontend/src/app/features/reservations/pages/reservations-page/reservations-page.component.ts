import { Component } from '@angular/core';
import { Router } from '@angular/router';

import {
    ReservationFormComponent,
    ReservationGameItem,
    ReservationTableOption
} from '../../../../shared/organisms/reservation-form/reservation-form.component';

@Component({
    selector: 'app-reservations-page',
    standalone: true,
    imports: [ReservationFormComponent],
    templateUrl: './reservations-page.component.html',
    styleUrl: './reservations-page.component.css'
})
export class ReservationsPageComponent {

    fecha = '';
    hora = '';
    mesaSeleccionada = '';

    juegos: ReservationGameItem[] = [];
    mesas: ReservationTableOption[] = [];

    loading = false;
    submitting = false;
    errorMessage = '';

    minDate = this.obtenerFechaActual();

    constructor(private router: Router) {}

    private obtenerFechaActual(): string {

        const fecha = new Date();

        const year = fecha.getFullYear();
        const month = String(fecha.getMonth() + 1).padStart(2, '0');
        const day = String(fecha.getDate()).padStart(2, '0');

        return `${year}-${month}-${day}`;
    }

    onGameQuantityChange(event: {
        numSerie: string;
        cantidad: number;
    }): void {

        this.juegos = this.juegos.map(game =>
            game.numSerie === event.numSerie
                ? { ...game, cantidad: event.cantidad }
                : game
        );
    }

    onCancel(): void {
        this.router.navigate(['/catalogo']);
    }

    onRetry(): void {
        this.errorMessage = '';
    }
}
