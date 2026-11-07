import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Socio, SocioService } from '../../../../core/services/socio.service';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ClienteResponse } from '../../../../core/models/cliente.model';
import { Membresia, MembresiaService, PlanMembresia } from '../../../../core/services/membresia.service';
import { mensajeError } from '../../../../core/utils/http-error';
import { fechaCorta, fechaISO, hoyISO, pesos, sumarMeses } from '../../../../core/utils/fecha';

interface FormSocio {
  documento: string;
  fechaAfiliacion: string;
  /** Al crear: registrar también la primera membresía. */
  conMembresia: boolean;
  plan: string;
  duracion: number;
}

/**
 * Socios: se elige un cliente ya registrado y, en el mismo paso,
 * se le puede crear su primera membresía (plan, tarifa y vigencia).
 */
@Component({
  selector: 'app-socios-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <section class="cl-page">
    <header class="cl-page-header">
      <div>
        <span class="cl-eyebrow">Administración · Clientes</span>
        <h1 class="cl-title">Socios</h1>
        <p class="cl-subtitle">Clientes afiliados. Con una membresía activa pueden pedir préstamos externos.</p>
      </div>
      <div class="cl-page-header__aside">
        <span class="cl-count"><strong>{{ socios.length }}</strong>registros</span>
        <button class="btn btn--gold" type="button" (click)="nuevo()">+ Nuevo socio</button>
      </div>
    </header>

    <p class="cl-alert cl-alert--success" *ngIf="mensaje">{{ mensaje }}</p>
    <p class="cl-alert cl-alert--error" *ngIf="error && !formAbierto">{{ error }}</p>

    <section class="cl-card">
      <div class="cl-toolbar">
        <input class="form-control" type="date" [(ngModel)]="fechaConsulta" aria-label="Fecha de afiliación">
        <button class="btn btn--gold" type="button" (click)="cargar()">Buscar</button>
        <button class="btn btn--ghost" type="button" (click)="fechaConsulta='';cargar()">Ver todos</button>
      </div>

      <p class="cl-empty" *ngIf="cargando">Consultando socios...</p>
      <p class="cl-empty" *ngIf="!cargando && socios.length === 0">No hay socios para mostrar.</p>
      <div class="cl-table-wrap" *ngIf="!cargando && socios.length">
        <table class="cl-table">
          <thead><tr><th>Documento</th><th>Nombre</th><th>Afiliación</th><th>Membresía</th><th class="cl-table__actions-head">Acciones</th></tr></thead>
          <tbody>
            @for (s of socios; track s.documento) {
              <tr>
                <td>{{ s.documento }}</td>
                <td>{{ nombres[s.documento] || '—' }}</td>
                <td>{{ formatearFecha(s.fechaAfiliacion) }}</td>
                <td>
                  @if (membresiaDe(s.documento); as m) {
                    <span class="badge" [ngClass]="m.estado === 'Activa' ? 'badge--success' : 'badge--danger'">
                      {{ m.plan }} · {{ m.estado }}
                    </span>
                    <span class="cl-muted"> hasta {{ formatearFecha(m.fechaFin) }}</span>
                  } @else {
                    <span class="cl-muted">Sin membresía</span>
                  }
                </td>
                <td>
                  <div class="cl-table__actions">
                    <button class="btn btn--ghost btn--sm" type="button" (click)="editar(s)">Editar</button>
                    <button class="btn btn--danger btn--sm" type="button" (click)="borrar(s)">Eliminar</button>
                  </div>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </section>

    <div class="cl-modal-overlay" *ngIf="formAbierto" (click)="cancelarEdicion()">
      <section class="cl-card cl-modal" role="dialog" aria-modal="true" (click)="$event.stopPropagation()">
        <h2 class="cl-card__title">{{ editando ? 'Editar socio' : 'Nuevo socio' }}</h2>
        <p class="cl-alert cl-alert--error" *ngIf="error">{{ error }}</p>
        <form class="cl-form-grid" (ngSubmit)="guardar()">
          <label class="cl-field cl-field--full">Cliente *
            <select class="form-control" name="documento" [(ngModel)]="form.documento" [disabled]="editando" required>
              <option value="" disabled>Selecciona un cliente</option>
              @for (c of opcionesCliente; track c.value) {
                <option [value]="c.value">{{ c.label }}</option>
              }
            </select>
            <span class="cl-field__hint" *ngIf="!editando">Solo aparecen los clientes que aún no son socios.</span>
          </label>
          <label class="cl-field cl-field--full">Fecha de afiliación *
            <input class="form-control" type="date" name="fecha" [(ngModel)]="form.fechaAfiliacion" [max]="hoy" required>
          </label>

          @if (!editando) {
            <label class="cl-check cl-field--full">
              <input type="checkbox" name="conMembresia" [(ngModel)]="form.conMembresia">
              Crear también su membresía
            </label>
            @if (form.conMembresia) {
              <label class="cl-field">Plan *
                <select class="form-control" name="plan" [(ngModel)]="form.plan" required>
                  <option value="" disabled>Selecciona un plan</option>
                  @for (p of planes; track p.plan) { <option [value]="p.plan">{{ p.plan }}</option> }
                </select>
              </label>
              <label class="cl-field">Duración *
                <select class="form-control" name="duracion" [(ngModel)]="form.duracion">
                  @for (d of duraciones; track d) {
                    <option [ngValue]="d">{{ d }} {{ d === 1 ? 'mes' : 'meses' }}</option>
                  }
                </select>
              </label>
              <div class="cl-field">Tarifa mensual
                <span class="cl-readonly">{{ tarifaTexto }}</span>
              </div>
              <div class="cl-field">Vence el
                <span class="cl-readonly">{{ venceTexto }}</span>
              </div>
            }
          }

          <div class="cl-form-actions">
            <button class="btn btn--ghost" type="button" (click)="cancelarEdicion()">Cancelar</button>
            <button class="btn btn--gold" type="submit" [disabled]="guardando">
              {{ guardando ? 'Guardando...' : 'Guardar socio' }}
            </button>
          </div>
        </form>
      </section>
    </div>
  </section>
  `,
  styles: [':host { display: block; }']
})
export class SociosPageComponent implements OnInit {
  socios: Socio[] = [];
  /** Vacío = todos los socios. */
  fechaConsulta = '';
  readonly hoy = hoyISO();
  readonly duraciones = [1, 3, 6, 12];
  form: FormSocio = this.vacio();
  /** Documento -> nombre del cliente, para mostrar quién es cada socio. */
  nombres: Record<string, string> = {};
  private clientesLista: ClienteResponse[] = [];
  private membresias: Membresia[] = [];
  planes: PlanMembresia[] = [];
  editando = false;
  formAbierto = false;
  guardando = false;
  cargando = false;
  mensaje = '';
  error = '';

  constructor(
    private service: SocioService,
    private clientes: ClienteService,
    private membresiaService: MembresiaService
  ) {}

  ngOnInit(): void {
    this.cargar();
    forkJoin({
      clientes: this.clientes.listar().pipe(catchError(() => of([] as ClienteResponse[]))),
      planes: this.membresiaService.planes().pipe(catchError(() => of([] as PlanMembresia[])))
    }).subscribe(({ clientes, planes }) => {
      this.clientesLista = clientes;
      clientes.forEach(c => this.nombres[c.documentoCliente] =
        [c.primerNombre, c.primerApellido].filter(Boolean).join(' '));
      this.planes = planes;
    });
  }

  private vacio(): FormSocio {
    return { documento: '', fechaAfiliacion: hoyISO(), conMembresia: true, plan: '', duracion: 1 };
  }

  /** Clientes que todavía no son socios (al editar, el socio actual). */
  get opcionesCliente(): { value: string; label: string }[] {
    if (this.editando) {
      return [{ value: this.form.documento, label: `${this.nombres[this.form.documento] || 'Socio'} · ${this.form.documento}` }];
    }
    const socios = new Set(this.socios.map(s => s.documento));
    return this.clientesLista
      .filter(c => !socios.has(c.documentoCliente))
      .map(c => ({ value: c.documentoCliente, label: `${this.nombres[c.documentoCliente]} · ${c.documentoCliente}` }))
      .sort((a, b) => a.label.localeCompare(b.label));
  }

  /** Membresía más reciente del socio (la activa si tiene una). */
  membresiaDe(documento: string): Membresia | null {
    const suyas = this.membresias.filter(m => m.documento === documento);
    return suyas.find(m => m.estado === 'Activa')
      ?? suyas.sort((a, b) => b.idMembresia - a.idMembresia)[0]
      ?? null;
  }

  get tarifaTexto(): string {
    const plan = this.planes.find(p => p.plan === this.form.plan);
    return plan ? pesos(plan.tarifa) + ' al mes' : 'Elige un plan';
  }

  get venceTexto(): string {
    return fechaCorta(sumarMeses(this.form.fechaAfiliacion, this.form.duracion));
  }

  formatearFecha(value: unknown): string {
    return fechaCorta(value);
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    const peticion = this.fechaConsulta
      ? this.service.listarPorFecha(this.fechaConsulta)
      : this.service.listar();
    peticion.subscribe({
      next: data => {
        this.socios = data.map(s => ({ ...s, fechaAfiliacion: fechaISO(s.fechaAfiliacion) }));
        this.cargando = false;
      },
      error: e => this.fallar(e, 'No fue posible consultar los socios.')
    });
    this.membresiaService.listar().subscribe({
      next: m => this.membresias = m,
      error: () => this.membresias = []
    });
  }

  guardar(): void {
    if (!this.form.documento || !this.form.fechaAfiliacion) {
      this.error = 'Selecciona el cliente y la fecha de afiliación.'; return;
    }
    const crearMembresia = !this.editando && this.form.conMembresia;
    if (crearMembresia && !this.form.plan) {
      this.error = 'Selecciona el plan de la membresía.'; return;
    }
    this.guardando = true; this.error = ''; this.mensaje = '';
    const editando = this.editando;
    const datos = { ...this.form };
    const request = { documento: datos.documento, fechaAfiliacion: fechaISO(datos.fechaAfiliacion) };
    const peticion = editando
      ? this.service.actualizar(request.documento, request)
      : this.service.crear(request);

    peticion.subscribe({
      next: () => {
        if (!crearMembresia) {
          this.terminar(editando ? 'Socio actualizado.' : 'Socio registrado.');
          return;
        }
        const plan = this.planes.find(p => p.plan === datos.plan);
        this.membresiaService.crear({
          idMembresia: 0,
          documento: datos.documento,
          plan: datos.plan,
          estado: 'Activa',
          fechaInicio: request.fechaAfiliacion,
          fechaFin: sumarMeses(request.fechaAfiliacion, datos.duracion),
          tarifa: plan?.tarifa ?? 0
        }).subscribe({
          next: () => this.terminar('Socio registrado con su membresía ' + datos.plan + '.'),
          error: e => {
            this.terminar('Socio registrado.');
            this.error = 'La membresía no se pudo crear: ' + mensajeError(e, 'error desconocido')
              + ' Puedes crearla en Membresías.';
          }
        });
      },
      error: e => this.fallar(e, 'No fue posible guardar el socio.')
    });
  }

  private terminar(mensaje: string): void {
    this.guardando = false;
    this.mensaje = mensaje;
    this.form = this.vacio();
    this.editando = false;
    this.formAbierto = false;
    this.cargar();
  }

  nuevo(): void {
    this.form = this.vacio();
    this.editando = false;
    this.formAbierto = true;
    this.error = ''; this.mensaje = '';
  }

  editar(s: Socio): void {
    this.form = { ...this.vacio(), documento: s.documento, fechaAfiliacion: fechaISO(s.fechaAfiliacion), conMembresia: false };
    this.editando = true;
    this.formAbierto = true;
    this.error = ''; this.mensaje = '';
  }

  cancelarEdicion(): void {
    this.form = this.vacio();
    this.editando = false;
    this.formAbierto = false;
    this.error = '';
  }

  borrar(s: Socio): void {
    if (!confirm(`¿Eliminar el socio ${this.nombres[s.documento] || s.documento}?`)) return;
    this.error = ''; this.mensaje = '';
    this.service.eliminar(s.documento).subscribe({
      next: () => { this.mensaje = 'Socio eliminado.'; this.cargar(); },
      error: e => this.fallar(e, 'No fue posible eliminar el socio. Si tiene membresías, elimínalas primero.')
    });
  }

  private fallar(e: unknown, texto: string): void {
    this.cargando = false;
    this.guardando = false;
    this.error = mensajeError(e, texto);
  }
}
