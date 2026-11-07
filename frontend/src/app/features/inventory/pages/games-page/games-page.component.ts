import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JuegoMesaService } from '../../../../core/services/juego-mesa.service';
import { JuegoMesaResponse } from '../../../../core/models/juego-mesa.model';
import { UnidadJuegoService } from '../../../../core/services/unidad-juego.service';
import { from } from 'rxjs';
import { concatMap, toArray } from 'rxjs/operators';

import { mensajeError } from '../../../../core/utils/http-error';
@Component({
  selector: 'app-games-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <section class="cl-page">
    <header class="cl-page-header">
      <div>
        <span class="cl-eyebrow">Administración · Juegos</span>
        <h1 class="cl-title">Juegos de mesa</h1>
        <p class="cl-subtitle">Catálogo general de juegos. Cada copia física se registra en Unidades físicas.</p>
      </div>
      <div class="cl-page-header__aside">
        <span class="cl-count"><strong>{{ juegos.length }}</strong>registros</span>
        <button class="btn btn--gold" type="button" (click)="nuevo()">+ Nuevo juego</button>
      </div>
    </header>

    <p class="cl-alert cl-alert--success" *ngIf="mensaje">{{ mensaje }}</p>
    <p class="cl-alert cl-alert--error" *ngIf="error && !formAbierto">{{ error }}</p>

    <section class="cl-card">
      <form class="cl-toolbar" (ngSubmit)="cargar()">
        <input class="form-control" name="busqueda" [(ngModel)]="busqueda" placeholder="Buscar por nombre (vacío = todos)">
        <button class="btn btn--gold" type="submit">Buscar</button>
        <button class="btn btn--ghost" type="button" (click)="busqueda='';cargar()">Ver todos</button>
      </form>

      <p class="cl-empty" *ngIf="cargando">Consultando juegos...</p>
      <p class="cl-empty" *ngIf="!cargando && !juegos.length">No hay juegos para mostrar.</p>
      <div class="cl-table-wrap" *ngIf="!cargando && juegos.length">
        <table class="cl-table">
          <thead>
            <tr><th>Número de serie</th><th>Nombre</th><th>Jugadores</th><th>Edad mínima</th><th>Duración</th><th>Disponibles</th><th class="cl-table__actions-head">Acciones</th></tr>
          </thead>
          <tbody>
            @for (j of juegos; track j.numSerie) {
              <tr>
                <td>{{ j.numSerie }}</td>
                <td>{{ j.nombre }}</td>
                <td>{{ j.cantidadMinimaJugadores }} – {{ j.cantidadMaximaJugadores }}</td>
                <td>{{ j.edadMinima }}+</td>
                <td>{{ j.duracionPartida }} min</td>
                <td>
                  <span class="badge" [ngClass]="(disponibles[j.numSerie] || 0) > 0 ? 'badge--success' : 'badge--danger'">
                    {{ disponibles[j.numSerie] || 0 }}
                  </span>
                </td>
                <td>
                  <div class="cl-table__actions">
                    <button class="btn btn--ghost btn--sm" type="button" (click)="editar(j)">Editar</button>
                    <button class="btn btn--danger btn--sm" type="button" (click)="eliminar(j)">Eliminar</button>
                  </div>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </section>

    <div class="cl-modal-overlay" *ngIf="formAbierto" (click)="cancelar()">
      <section class="cl-card cl-modal" role="dialog" aria-modal="true" (click)="$event.stopPropagation()">
        <h2 class="cl-card__title">{{ editando ? 'Editar juego' : 'Nuevo juego' }}</h2>
        <p class="cl-alert cl-alert--error" *ngIf="error">{{ error }}</p>
        <form class="cl-form-grid" (ngSubmit)="guardar()">
          <label class="cl-field">Número de serie *
            <input class="form-control" name="serie" maxlength="30" [(ngModel)]="form.numSerie" required [disabled]="editando" placeholder="Ej: CATAN-CL-2024-112">
          </label>
          <label class="cl-field">Nombre *
            <input class="form-control" name="nombre" [(ngModel)]="form.nombre" required>
          </label>
          <label class="cl-field cl-field--full">Reglamento *
            <input class="form-control" name="reglamento" [(ngModel)]="form.reglamento" required placeholder="Enlace o descripción del reglamento">
          </label>
          <label class="cl-field">Jugadores mínimos *
            <input class="form-control" type="number" name="min" [(ngModel)]="form.cantidadMinimaJugadores" min="1" required>
          </label>
          <label class="cl-field">Jugadores máximos *
            <input class="form-control" type="number" name="max" [(ngModel)]="form.cantidadMaximaJugadores" min="1" required>
          </label>
          <label class="cl-field">Edad mínima *
            <input class="form-control" type="number" name="edad" [(ngModel)]="form.edadMinima" min="1" required>
          </label>
          <label class="cl-field">Duración (minutos) *
            <input class="form-control" type="number" name="duracion" [(ngModel)]="form.duracionPartida" min="1" required>
          </label>
          <label class="cl-field cl-field--full" *ngIf="!editando">Unidades físicas que llegan
            <input class="form-control" type="number" name="unidades" [(ngModel)]="unidadesNuevas" min="0" max="20">
            <span class="cl-field__hint">Se registran como Disponibles con código automático (UJ-…-001, 002…). Puedes poner 0 y agregarlas después.</span>
          </label>
          <div class="cl-form-actions">
            <button class="btn btn--ghost" type="button" (click)="cancelar()">Cancelar</button>
            <button class="btn btn--gold" type="submit" [disabled]="guardando">{{ guardando ? 'Guardando...' : 'Guardar juego' }}</button>
          </div>
        </form>
      </section>
    </div>
  </section>

  `,
  styles: [':host { display: block; }']
})
export class GamesPageComponent implements OnInit {
  juegos: JuegoMesaResponse[] = [];
  busqueda = '';
  cargando = false;
  guardando = false;
  editando = false;
  formAbierto = false;
  error = '';
  mensaje = '';

  form: JuegoMesaResponse = this.vacio();
  /** Cuántas copias físicas registrar al crear el juego. */
  unidadesNuevas = 1;
  /** numSerie -> unidades disponibles. */
  disponibles: Record<string, number> = {};

  constructor(private service: JuegoMesaService, private unidades: UnidadJuegoService) {}

  ngOnInit(): void { this.cargar(); }

  private vacio(): JuegoMesaResponse {
    return {
      numSerie: '', nombre: '', reglamento: '',
      cantidadMinimaJugadores: 1,
      cantidadMaximaJugadores: 4,
      edadMinima: 8, duracionPartida: 30
    };
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.service.listar(this.busqueda).subscribe({
      next: data => { this.juegos = data; this.cargando = false; },
      error: e => { this.error = mensajeError(e, 'Error al consultar juegos.'); this.cargando = false; }
    });
    this.unidades.consultarDisponibilidad().subscribe({
      next: lista => {
        this.disponibles = {};
        lista.forEach(d => this.disponibles[d.numSerie] = d.unidadesDisponibles);
      },
      error: () => this.disponibles = {}
    });
  }

  guardar(): void {
    // Mismas reglas que valida el backend (JuegoMesaService)
    if (!this.form.numSerie.trim() || !this.form.nombre.trim() || !this.form.reglamento?.trim()) {
      this.error = 'Número de serie, nombre y reglamento son obligatorios.';
      return;
    }
    if (this.form.cantidadMinimaJugadores < 1 ||
        this.form.cantidadMaximaJugadores < this.form.cantidadMinimaJugadores) {
      this.error = 'Revisa la cantidad de jugadores: mínimo 1 y el máximo no puede ser menor que el mínimo.';
      return;
    }
    if (this.form.edadMinima < 1 || this.form.duracionPartida < 1) {
      this.error = 'La edad mínima y la duración deben ser mayores que cero.';
      return;
    }

    this.guardando = true;
    this.error = '';
    this.mensaje = '';
    const editando = this.editando;
    const data = { ...this.form };
    const request = editando
      ? this.service.actualizar(data.numSerie, data)
      : this.service.crear(data);

    const copias = editando ? 0 : Math.max(0, Math.min(20, Math.floor(Number(this.unidadesNuevas) || 0)));
    request.subscribe({
      next: () => {
        if (!copias) {
          this.terminarGuardado(editando ? 'Juego actualizado.' : 'Juego registrado.');
          return;
        }
        // Se crean una a una para que cada código sea consecutivo
        from(Array.from({ length: copias })).pipe(
          concatMap(() => this.unidades.crear({ codigo: '', numSerie: data.numSerie, estado: 'DISPONIBLE', observaciones: '' })),
          toArray()
        ).subscribe({
          next: () => this.terminarGuardado(`Juego registrado con ${copias} ${copias === 1 ? 'unidad' : 'unidades'} disponible${copias === 1 ? '' : 's'}.`),
          error: e => {
            this.terminarGuardado('Juego registrado.');
            this.error = 'No se pudieron crear todas las unidades: ' + mensajeError(e, 'error desconocido');
          }
        });
      },
      error: e => {
        this.guardando = false;
        this.error = mensajeError(e, 'No fue posible guardar el juego.');
      }
    });
  }

  private terminarGuardado(mensaje: string): void {
    this.guardando = false;
    this.mensaje = mensaje;
    this.cancelar();
    this.cargar();
  }

  nuevo(): void {
    this.unidadesNuevas = 1;
    this.form = this.vacio();
    this.editando = false;
    this.formAbierto = true;
    this.mensaje = '';
    this.error = '';
  }

  editar(j: JuegoMesaResponse): void {
    this.form = { ...j };
    this.editando = true;
    this.formAbierto = true;
    this.mensaje = '';
    this.error = '';
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editando = false;
    this.formAbierto = false;
  }

  eliminar(j: JuegoMesaResponse): void {
    if (!confirm(`¿Eliminar el juego ${j.nombre}?`)) return;
    this.service.eliminar(j.numSerie).subscribe({
      next: () => { this.mensaje = 'Juego eliminado.'; this.cargar(); },
      error: e => { this.error = mensajeError(e, 'No fue posible eliminar el juego.'); }
    });
  }
}
