import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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
import { mensajeError } from '../../../../core/utils/http-error';
import { hoyISO } from '../../../../core/utils/fecha';
import { Opcion, OpcionesService } from '../../../../core/services/opciones.service';
import { ReservationsManagementComponent } from '../../components/reservations-management/reservations-management.component';

@Component({
  selector: 'app-reservations-page',
  standalone: true,
  imports: [CommonModule, FormsModule, ReservationFormComponent, ReservationsManagementComponent],
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
  /** Error al cargar catálogo o mesas (bloquea el formulario y ofrece reintentar). */
  errorMessage = '';
  /** Error de validación o al guardar (se muestra sin ocultar el formulario). */
  submitError = '';
  mensaje = '';

  /** Vista de empleado: muestra además el listado de reservas. */
  esEmpleado = false;
  /** Para el empleado: clientes en una lista en vez de escribir el documento. */
  clientes: Opcion[] = [];
  private readonly opciones = inject(OpcionesService);
  /** Se incrementa para que el listado de empleado se recargue. */
  recargarListado = 0;

  minDate = hoyISO();

  /** Juegos que el cliente marcó en el catálogo antes de llegar aquí. */
  private preseleccion = new Set<string>();

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private juegosService: JuegoMesaService,
    private unidadesService: UnidadJuegoService,
    private mesasService: MesaService,
    private reservasService: ReservaService
  ) {
    const estado = this.router.getCurrentNavigation()?.extras.state ?? history.state;
    const ids = estado?.['selectedGameIds'];
    if (Array.isArray(ids)) {
      ids.forEach((id: unknown) => typeof id === 'string' && this.preseleccion.add(id));
    }
  }

  ngOnInit(): void {
    this.esEmpleado = this.route.snapshot.data['modo'] === 'empleado';
    if (this.esEmpleado) this.opciones.clientes().subscribe(c => this.clientes = c);
    this.cargarCatalogo();
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
            // Solo quedan marcados los juegos elegidos en el catálogo
            cantidad: this.preseleccion.has(j.numSerie) ? 1 : 0
          };
        });
        this.loading = false;
      },
      error: e => {
        this.loading = false;
        this.errorMessage = mensajeError(e,
          'No fue posible consultar juegos y disponibilidad.');
      }
    });
  }

  cargarMesasDisponibles(): void {
    if (!this.fecha || !this.hora) return;

    this.submitError = '';

    this.mesasService.disponibles(this.fecha, this.hora).subscribe({
      next: data => {
        this.mesas = data.map(m => ({
          idMesa: String(m.idMesa),
          label: `Mesa ${m.idMesa} · ${m.tematica} · ${m.ubicacion}`,
          capacidad: m.capacidad,
          tematica: m.tematica,
          ubicacion: m.ubicacion,
          disponible: true
        }));

        if (!this.mesas.some(m => m.idMesa === this.mesaSeleccionada)) {
          this.mesaSeleccionada = '';
        }
        if (!this.mesas.length) {
          this.submitError = 'No hay mesas libres en esa fecha y hora. Prueba otro horario.';
        }
      },
      error: e => {
        this.mesas = [];
        this.submitError = mensajeError(e,
          'No fue posible consultar las mesas disponibles.');
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

  onGameQuantityChange(event: { numSerie: string; cantidad: number }): void {
    this.juegos = this.juegos.map(j =>
      j.numSerie === event.numSerie ? { ...j, cantidad: event.cantidad } : j
    );
    this.submitError = '';
  }

  onDocumentoChange(): void {
    this.submitError = '';
    this.mensaje = '';
  }

  onConfirm(payload: ReservationPayload): void {
    this.mensaje = '';
    const documento = this.documento.trim();

    if (!documento) {
      this.submitError = 'Ingresa el documento del cliente que hace la reserva.';
      return;
    }

    const seleccionados = payload.juegos
      .filter(j => j.cantidad > 0)
      .map(j => j.numSerie);

    if (!seleccionados.length) {
      this.submitError = 'Selecciona al menos un juego.';
      return;
    }

    const request = {
      // El backend genera el identificador de la reserva
      idReserva: '',
      fecha: payload.fecha,
      horario: payload.horario,
      documento,
      idMesa: payload.idMesa,
      juegos: seleccionados
    };

    this.submitting = true;
    this.submitError = '';

    this.reservasService.crear(request).subscribe({
      next: reserva => {
        this.submitting = false;
        this.mensaje = `Reserva #${reserva.idReserva} registrada para el ${payload.fecha} a las ${payload.horario}.`;
        this.fecha = '';
        this.hora = '';
        this.mesaSeleccionada = '';
        this.mesas = [];
        this.documento = '';
        this.preseleccion.clear();
        this.recargarListado++;
        this.cargarCatalogo();
      },
      error: e => {
        this.submitting = false;
        this.submitError = mensajeError(e, 'No fue posible registrar la reserva.');
      }
    });
  }

  onCancel(): void {
    this.router.navigate([this.esEmpleado ? '/empleado/catalogo' : '/catalogo']);
  }

  onRetry(): void {
    this.errorMessage = '';
    this.cargarCatalogo();
  }
}
