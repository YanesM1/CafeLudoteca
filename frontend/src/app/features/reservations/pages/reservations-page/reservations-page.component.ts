import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';

import {
  ReservationFormComponent,
  ReservationGameItem,
  ReservationTableOption,
  ReservationPayload
} from '../../../../shared/organisms/reservation-form/reservation-form.component';

import { JuegoMesaService } from '../../../../core/services/juego-mesa.service';
import { UnidadJuegoService } from '../../../../core/services/unidad-juego.service';
import { MesaService } from '../../../../core/services/mesa.service';
import { ReservaService } from '../../../../core/services/reserva.service';

@Component({
  selector: 'app-reservations-page',
  standalone: true,
  imports: [CommonModule, FormsModule, ReservationFormComponent],
  templateUrl: './reservations-page.component.html',
  styleUrl: './reservations-page.component.css'
})
export class ReservationsPageComponent implements OnInit {
  fecha = '';
  hora = '';
  mesaSeleccionada = '';
  documento = '';

  juegos: ReservationGameItem[] = [];
  mesas: ReservationTableOption[] = [];

  loading = false;
  submitting = false;
  errorMessage = '';
  mensaje = '';

  minDate = this.obtenerFechaActual();

  constructor(
    private router: Router,
    private juegosService: JuegoMesaService,
    private unidadesService: UnidadJuegoService,
    private mesasService: MesaService,
    private reservasService: ReservaService
  ) {}

  ngOnInit(): void {
    this.cargarCatalogo();
  }

  private obtenerFechaActual(): string {
    const f = new Date();
    return `${f.getFullYear()}-${String(f.getMonth()+1).padStart(2,'0')}-${String(f.getDate()).padStart(2,'0')}`;
  }

  cargarCatalogo(): void {
    this.loading = true;
    this.errorMessage = '';

    forkJoin({
      juegos: this.juegosService.listar(),
      unidades: this.unidadesService.consultarDisponibilidad()
    }).subscribe({
      next: r => {
        this.juegos = r.juegos.map(j => {
          const unidad = r.unidades.find(u => u.numSerie === j.numSerie);
          const disponibles = unidad?.unidadesDisponibles ?? 0;
          return {
            numSerie: j.numSerie,
            nombre: j.nombre,
            unidadesDisponibles: disponibles,
            cantidad: disponibles > 0 ? 1 : 0
          };
        });
        this.loading = false;
      },
      error: e => {
        this.loading = false;
        this.errorMessage = e.error?.mensaje ||
          'No fue posible consultar juegos y disponibilidad.';
      }
    });
  }

  cargarMesasDisponibles(): void {
    if (!this.fecha || !this.hora) return;

    this.loading = true;
    this.errorMessage = '';

    this.mesasService.disponibles(this.fecha, this.hora).subscribe({
      next: data => {
        this.mesas = data.map(m => ({
          idMesa: m.idMesa,
          label: `${m.tematica} · ${m.ubicacion}`,
          capacidad: m.capacidad,
          tematica: m.tematica,
          ubicacion: m.ubicacion,
          disponible: true
        }));

        if (!this.mesas.some(m => m.idMesa === this.mesaSeleccionada)) {
          this.mesaSeleccionada = '';
        }
        this.loading = false;
      },
      error: e => {
        this.loading = false;
        this.errorMessage = e.error?.mensaje ||
          'No fue posible consultar las mesas disponibles.';
      }
    });
  }

  onDateChange(value: string): void {
    this.fecha = value;
    this.mesaSeleccionada = '';
    this.cargarMesasDisponibles();
  }

  onTimeChange(value: string): void {
    this.hora = value;
    this.mesaSeleccionada = '';
    this.cargarMesasDisponibles();
  }

  onGameQuantityChange(event: {
    numSerie: string;
    cantidad: number;
  }): void {
    this.juegos = this.juegos.map(j =>
      j.numSerie === event.numSerie
        ? {...j, cantidad: event.cantidad}
        : j
    );
  }

  onConfirm(payload: ReservationPayload): void {
    if (!/^[0-9]{5,15}$/.test(this.documento.trim())) {
      this.errorMessage = 'Ingresa un documento válido.';
      return;
    }

    const seleccionados = payload.juegos
      .filter(j => j.cantidad > 0)
      .map(j => j.numSerie);

    if (!seleccionados.length) {
      this.errorMessage = 'Selecciona al menos un juego disponible.';
      return;
    }

    const request = {
      idReserva: `RES-${Date.now()}`,
      fecha: payload.fecha,
      horario: payload.horario,
      documento: this.documento.trim(),
      idMesa: payload.idMesa,
      juegos: seleccionados
    };

    this.submitting = true;
    this.errorMessage = '';

    this.reservasService.crear(request).subscribe({
      next: () => {
        this.submitting = false;
        this.mensaje = `Reserva ${request.idReserva} registrada correctamente.`;
        this.fecha = '';
        this.hora = '';
        this.mesaSeleccionada = '';
        this.mesas = [];
        this.documento = '';
        this.cargarCatalogo();
      },
      error: e => {
        this.submitting = false;
        this.errorMessage = e.error?.mensaje ||
          'No fue posible registrar la reserva.';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/catalogo']);
  }

  onRetry(): void {
    this.errorMessage = '';
    this.cargarCatalogo();
  }
}
