import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ReservaResponse, ReservaService } from '../../../../core/services/reserva.service';
import { fechaCorta, hoyISO } from '../../../../core/utils/fecha';
import { mensajeError } from '../../../../core/utils/http-error';
import { OpcionesService } from '../../../../core/services/opciones.service';

/**
 * Listado de reservas para el empleado: consultar por fecha o por cliente
 * y cancelar reservas (RF-09, RF-11).
 */
@Component({
  selector: 'app-reservations-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <section class="cl-card">
    <div class="cl-card__header">
      <h2 class="cl-card__title">Reservas registradas</h2>
      <span class="cl-count"><strong>{{ reservas.length }}</strong>reservas</span>
    </div>

    <div class="cl-toolbar">
      <input class="form-control" type="date" [(ngModel)]="fecha" name="fechaFiltro" aria-label="Fecha">
      <input class="form-control" [(ngModel)]="documento" name="documentoFiltro" placeholder="Documento del cliente (opcional)">
      <button class="btn btn--gold" type="button" (click)="cargar()">Buscar</button>
      <button class="btn btn--ghost" type="button" (click)="verTodas()">Ver todas</button>
    </div>

    <p class="cl-alert cl-alert--error" *ngIf="error">{{ error }}</p>
    <p class="cl-alert cl-alert--success" *ngIf="mensaje">{{ mensaje }}</p>
    <p class="cl-empty" *ngIf="cargando">Consultando reservas...</p>
    <p class="cl-empty" *ngIf="!cargando && !reservas.length">No hay reservas para el filtro seleccionado.</p>

    <div class="cl-table-wrap" *ngIf="!cargando && reservas.length">
      <table class="cl-table">
        <thead>
          <tr><th>#</th><th>Fecha</th><th>Hora</th><th>Cliente</th><th>Mesa</th><th>Juegos</th><th class="cl-table__actions-head">Acciones</th></tr>
        </thead>
        <tbody>
          @for (r of reservas; track r.idReserva) {
            <tr>
              <td>{{ r.idReserva }}</td>
              <td>{{ fechaCorta(r.fecha) }}</td>
              <td>{{ hora(r.horario) }}</td>
              <td>{{ nombres[r.documento] || r.documento }} <span class="cl-muted">· {{ r.documento }}</span></td>
              <td>{{ r.idMesa }}</td>
              <td>{{ juegosTexto(r.juegos) }}</td>
              <td><div class="cl-table__actions">
                <button class="btn btn--danger btn--sm" type="button" (click)="cancelar(r)">Cancelar</button>
              </div></td>
            </tr>
          }
        </tbody>
      </table>
    </div>
  </section>`,
  styles: [':host { display: block; }']
})
export class ReservationsManagementComponent implements OnChanges {
  /** Cambia cuando se crea una reserva nueva para recargar el listado. */
  @Input() recargar = 0;

  reservas: ReservaResponse[] = [];
  fecha = hoyISO();
  documento = '';
  cargando = false;
  error = '';
  mensaje = '';

  /** Nombres de clientes y juegos para mostrar en vez de códigos. */
  nombres: Record<string, string> = {};
  private juegos: Record<string, string> = {};
  private readonly opciones = inject(OpcionesService);

  constructor(private service: ReservaService) {
    this.opciones.nombresClientes().subscribe(n => this.nombres = n);
    this.opciones.juegos().subscribe(lista =>
      lista.forEach(j => this.juegos[j.value] = j.label.split(' · ')[0]));
  }

  juegosTexto(series: string[] | undefined): string {
    return (series || []).map(s => this.juegos[s] || s).join(', ') || '—';
  }

  ngOnChanges(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    const documento = this.documento.trim();
    const peticion = documento
      ? this.service.listar(documento)
      : this.service.listarTodas(this.fecha);

    peticion.subscribe({
      next: data => {
        const dia = this.fecha;
        // Si se busca por cliente y además hay fecha, se filtra ese día
        this.reservas = documento && dia
          ? data.filter(r => fechaCorta(r.fecha) === fechaCorta(dia))
          : data;
        this.cargando = false;
      },
      error: e => {
        this.cargando = false;
        this.error = mensajeError(e, 'No fue posible consultar las reservas.');
      }
    });
  }

  verTodas(): void {
    this.fecha = '';
    this.documento = '';
    this.cargar();
  }

  cancelar(r: ReservaResponse): void {
    if (!confirm(`¿Cancelar la reserva #${r.idReserva}?`)) return;
    this.error = '';
    this.service.eliminar(r.idReserva).subscribe({
      next: () => {
        this.mensaje = `Reserva #${r.idReserva} cancelada.`;
        this.cargar();
      },
      error: e => this.error = mensajeError(e, 'No fue posible cancelar la reserva.')
    });
  }

  fechaCorta(valor: unknown): string {
    return fechaCorta(valor);
  }

  hora(valor: string): string {
    return (valor || '').substring(0, 5);
  }
}
