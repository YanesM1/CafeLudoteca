import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Socio, SocioService } from '../../../../core/services/socio.service';

@Component({
  selector: 'app-socios-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <main class="page">
    <header class="heading">
      <div>
        <span class="eyebrow">GESTIÓN ADMINISTRATIVA</span>
        <h1>Socios</h1>
        <p>Registra y administra los socios de Café & Ludoteca.</p>
      </div>
      <strong class="count">{{ socios.length }} <small>registros</small></strong>
    </header>

    <section class="panel">
      <h2>{{ editando ? 'Editar socio' : 'Registrar nuevo socio' }}</h2>
      <form (ngSubmit)="guardar()">
        <label>Documento del cliente
          <input name="documento" [(ngModel)]="form.documento"
            [disabled]="editando" required placeholder="Número de documento">
        </label>
        <label>Fecha de afiliación
          <input type="date" name="fecha" [(ngModel)]="form.fechaAfiliacion" required>
        </label>
        <div class="actions">
          <button class="primary" type="submit" [disabled]="guardando">
            {{ guardando ? 'Guardando...' : editando ? 'Guardar cambios' : 'Registrar socio' }}
          </button>
          <button type="button" class="secondary" *ngIf="editando" (click)="cancelarEdicion()">Cancelar</button>
        </div>
      </form>
    </section>

    <section class="panel">
      <h2>Consultar socios por fecha de afiliación</h2>
      <div class="search">
        <input type="date" [(ngModel)]="fechaConsulta">
        <button class="primary" (click)="cargar()">Buscar</button>
      </div>
    </section>

    <p class="message" *ngIf="mensaje">{{ mensaje }}</p>
    <p class="error" *ngIf="error">{{ error }}</p>

    <section class="panel">
      <h2>Resultados</h2>
      <p *ngIf="cargando">Consultando socios...</p>
      <p *ngIf="!cargando && socios.length === 0">No hay socios para la fecha seleccionada.</p>
      <div class="table-wrap" *ngIf="socios.length">
        <table>
          <thead><tr><th>Documento</th><th>Fecha de afiliación</th><th>Acciones</th></tr></thead>
          <tbody>
            @for (s of socios; track s.documento) {
              <tr>
                <td>{{ s.documento }}</td>
                <td>{{ formatearFecha(s.fechaAfiliacion) }}</td>
                <td class="row-actions">
                  <button class="secondary" (click)="editar(s)">Editar</button>
                  <button class="danger" (click)="borrar(s)">Eliminar</button>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </section>
  </main>
  `,
  styles: [`
    .page{padding:32px 24px;color:#f5f5fa;background:#0b1020;min-height:calc(100vh - 100px)}
    .heading{display:flex;justify-content:space-between;align-items:center;margin-bottom:28px}
    .eyebrow{color:#22d3ee;font-weight:800;letter-spacing:2px;font-size:13px}
    h1{font-size:32px;margin:10px 0} h2{font-size:19px;margin:0 0 22px}
    p{color:#aab2c2}.count{font-size:30px;color:#22d3ee}.count small{font-size:13px;color:#aab2c2}
    .panel{background:#1b2030;border:1px solid #465267;border-radius:14px;padding:24px;margin-bottom:22px}
    form{display:grid;grid-template-columns:1fr 1fr;gap:20px}
    label{display:grid;gap:9px;color:#c6ccd7;font-weight:700}
    input{box-sizing:border-box;width:100%;background:#101827;border:1px solid #526078;color:white;border-radius:9px;padding:15px;font-size:16px}
    button{border:1px solid #22d3ee;border-radius:9px;padding:12px 18px;font-weight:700;cursor:pointer}
    button:disabled{opacity:.5;cursor:not-allowed}
    .primary{background:#d3b354;color:#171827;border-color:#d3b354}
    .secondary{background:transparent;color:#22d3ee}
    .danger{background:#a32154;color:white;border-color:#d12e70}
    .actions,.search,.row-actions{display:flex;gap:12px;align-items:center}
    .actions{grid-column:1/-1}.search input{max-width:260px}
    .message{color:#22d3ee}.error{color:#ff7090}
    .table-wrap{overflow-x:auto}table{width:100%;border-collapse:collapse}
    th,td{text-align:left;padding:15px;border-bottom:1px solid #465267}
    th{color:#22d3ee}
    @media(max-width:650px){form{grid-template-columns:1fr}.search,.row-actions{flex-wrap:wrap}.heading{align-items:flex-start}}
  `]
})
export class SociosPageComponent implements OnInit {
  socios: Socio[] = [];
  fechaConsulta = this.fechaLocal();
  form: Socio = { documento: '', fechaAfiliacion: this.fechaLocal() };
  editando = false;
  guardando = false;
  cargando = false;
  mensaje = '';
  error = '';

  constructor(private service: SocioService) {}

  ngOnInit(): void { this.cargar(); }

  private fechaLocal(): string {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`;
  }

  private normalizarFecha(value: string | number[]): string {
    if (Array.isArray(value)) {
      const [year, month, day] = value;
      return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    }
    return value.substring(0, 10);
  }

  formatearFecha(value: string | number[]): string {
    const fecha = this.normalizarFecha(value);
    const [year, month, day] = fecha.split('-');
    return `${day}/${month}/${year}`;
  }

  cargar(): void {
    if (!this.fechaConsulta) { this.error = 'Selecciona una fecha.'; return; }
    this.cargando = true; this.error = ''; this.mensaje = '';
    this.service.listarPorFecha(this.fechaConsulta).subscribe({
      next: data => {
        this.socios = data.map(s => ({
          ...s,
          fechaAfiliacion: this.normalizarFecha(s.fechaAfiliacion)
        }));
        this.cargando = false;
      },
      error: e => this.fallar(e, 'No fue posible consultar los socios.')
    });
  }

  guardar(): void {
    if (!this.form.documento.trim() || !this.form.fechaAfiliacion) {
      this.error = 'Completa el documento y la fecha.'; return;
    }
    this.guardando = true; this.error = ''; this.mensaje = '';
    const request = {
      documento: this.form.documento.trim(),
      fechaAfiliacion: this.form.fechaAfiliacion
    };
    const peticion = this.editando
      ? this.service.actualizar(request.documento, request)
      : this.service.crear(request);

    peticion.subscribe({
      next: () => {
        this.guardando = false;
        this.mensaje = this.editando ? 'Socio actualizado.' : 'Socio registrado.';
        this.form = { documento: '', fechaAfiliacion: this.fechaLocal() };
        this.editando = false;
        this.cargar();
      },
      error: e => { this.guardando = false; this.fallar(e, 'No fue posible guardar el socio.'); }
    });
  }

  editar(s: Socio): void {
    this.form = { ...s }; this.editando = true;
    this.error = ''; this.mensaje = '';
  }

  cancelarEdicion(): void {
    this.form = { documento: '', fechaAfiliacion: this.fechaLocal() };
    this.editando = false;
  }

  borrar(s: Socio): void {
    if (!confirm(`¿Eliminar el socio ${s.documento}?`)) return;
    this.service.eliminar(s.documento).subscribe({
      next: () => { this.mensaje = 'Socio eliminado.'; this.cargar(); },
      error: e => this.fallar(e, 'No fue posible eliminar el socio.')
    });
  }
private fallar(e: HttpErrorResponse, texto: string): void {
    this.cargando = false;
    this.guardando = false;
    this.error = e.error?.mensaje || e.error || texto;
  }
}
