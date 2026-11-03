import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JuegoMesaService } from '../../../../core/services/juego-mesa.service';
import { JuegoMesaResponse } from '../../../../core/models/juego-mesa.model';

@Component({
  selector: 'app-games-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <main class="page">
    <header>
      <div>
        <span class="eyebrow">ADMINISTRACIÓN · INVENTARIO</span>
        <h1>Juegos de mesa</h1>
        <p>Registra y administra el catálogo de juegos.</p>
      </div>
      <strong class="count">{{ juegos.length }} <small>juegos</small></strong>
    </header>

    <section class="panel">
      <h2>{{ editando ? 'Editar juego' : 'Registrar juego' }}</h2>
      <form (ngSubmit)="guardar()">
        <label>Número de serie
          <input name="serie" [(ngModel)]="form.numSerie" required
            [disabled]="editando" placeholder="Número de serie">
        </label>
        <label>Nombre
          <input name="nombre" [(ngModel)]="form.nombre" required>
        </label>
        <label>Reglamento
          <input name="reglamento" [(ngModel)]="form.reglamento">
        </label>
        <label>Jugadores mínimos
          <input type="number" name="min" [(ngModel)]="form.cantidadMinimaJugadores" min="1" required>
        </label>
        <label>Jugadores máximos
          <input type="number" name="max" [(ngModel)]="form.cantidadMaximaJugadores" min="1" required>
        </label>
        <label>Edad mínima
          <input type="number" name="edad" [(ngModel)]="form.edadMinima" min="0" required>
        </label>
        <label>Duración (minutos)
          <input type="number" name="duracion" [(ngModel)]="form.duracionPartida" min="1" required>
        </label>
        <div class="actions">
          <button class="primary" type="submit" [disabled]="guardando">
            {{ guardando ? 'Guardando...' : editando ? 'Guardar cambios' : 'Registrar juego' }}
          </button>
          <button class="secondary" type="button" *ngIf="editando" (click)="cancelar()">Cancelar</button>
        </div>
      </form>
    </section>

    <section class="panel">
      <h2>Catálogo de inventario</h2>
      <div class="search">
        <input [(ngModel)]="busqueda" placeholder="Buscar por nombre">
        <button class="primary" (click)="cargar()">Buscar</button>
        <button class="secondary" (click)="busqueda=''; cargar()">Limpiar</button>
      </div>
      <p class="error" *ngIf="error">{{ error }}</p>
      <p *ngIf="cargando">Consultando juegos...</p>
      <p *ngIf="!cargando && juegos.length===0">No se encontraron juegos.</p>
      <div class="table-wrap" *ngIf="juegos.length">
        <table>
          <thead>
            <tr><th>Serie</th><th>Nombre</th><th>Jugadores</th><th>Edad</th><th>Duración</th><th>Acciones</th></tr>
          </thead>
          <tbody>
            @for (j of juegos; track j.numSerie) {
              <tr>
                <td>{{ j.numSerie }}</td>
                <td>{{ j.nombre }}</td>
                <td>{{ j.cantidadMinimaJugadores }} – {{ j.cantidadMaximaJugadores }}</td>
                <td>{{ j.edadMinima }}+</td>
                <td>{{ j.duracionPartida }} min</td>
                <td class="actions">
                  <button class="secondary" (click)="editar(j)">Editar</button>
                  <button class="danger" (click)="eliminar(j)">Eliminar</button>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </section>
    <p class="message" *ngIf="mensaje">{{ mensaje }}</p>
  </main>
  `,
  styles: [`
    .page{padding:30px 24px;background:#0b1020;color:#f5f5fa;min-height:calc(100vh - 100px)}
    header{display:flex;justify-content:space-between;align-items:center;margin-bottom:25px}
    .eyebrow{color:#22d3ee;font-size:12px;font-weight:800;letter-spacing:2px}
    h1{font-size:32px;margin:10px 0}h2{font-size:19px;margin:0 0 20px}
    p{color:#aeb5c5}.count{font-size:27px;color:#22d3ee}.count small{font-size:13px}
    .panel{background:#1b2030;border:1px solid #465267;border-radius:14px;padding:24px;margin-bottom:22px}
    form{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:17px}
    label{display:grid;gap:8px;color:#c6ccd7;font-weight:700}
    input{width:100%;box-sizing:border-box;background:#101827;color:white;border:1px solid #526078;border-radius:8px;padding:13px}
    button{padding:11px 16px;border-radius:8px;border:1px solid #22d3ee;cursor:pointer;font-weight:700}
    button:disabled{opacity:.5}.primary{background:#d3b354;color:#171827;border-color:#d3b354}
    .secondary{background:transparent;color:#22d3ee}.danger{background:#a32154;color:white;border-color:#a32154}
    .actions,.search{display:flex;gap:10px;align-items:center}.actions{flex-wrap:wrap}
    form>.actions{grid-column:1/-1}.search{margin-bottom:20px}.search input{max-width:350px}
    table{width:100%;border-collapse:collapse}th,td{text-align:left;padding:13px;border-bottom:1px solid #465267}
    th{color:#22d3ee}.table-wrap{overflow-x:auto}.error{color:#ff7892}.message{color:#22d3ee}
    @media(max-width:650px){form{grid-template-columns:1fr}header{align-items:flex-start}.search{flex-wrap:wrap}}
  `]
})
export class GamesPageComponent implements OnInit {
  juegos: JuegoMesaResponse[] = [];
  busqueda = '';
  cargando = false;
  guardando = false;
  editando = false;
  error = '';
  mensaje = '';

  form: JuegoMesaResponse = this.vacio();

  constructor(private service: JuegoMesaService) {}

  ngOnInit(): void { this.cargar(); }

  private vacio(): JuegoMesaResponse {
    return {
      numSerie: '', nombre: '', reglamento: '',
      cantidadMinimaJugadores: 1,
      cantidadMaximaJugadores: 4,
      edadMinima: 0, duracionPartida: 30
    };
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.service.listar(this.busqueda).subscribe({
      next: data => { this.juegos = data; this.cargando = false; },
      error: e => { this.error = e.error?.mensaje || 'Error al consultar juegos.'; this.cargando = false; }
    });
  }

  guardar(): void {
    if (!this.form.numSerie.trim() || !this.form.nombre.trim() ||
        this.form.cantidadMinimaJugadores < 1 ||
        this.form.cantidadMaximaJugadores < this.form.cantidadMinimaJugadores ||
        this.form.edadMinima < 0 || this.form.duracionPartida < 1) {
      this.error = 'Revisa los datos ingresados.';
      return;
    }

    this.guardando = true;
    this.error = '';
    const data = { ...this.form };
    const request = this.editando
      ? this.service.actualizar(data.numSerie, data)
      : this.service.crear(data);

    request.subscribe({
      next: () => {
        this.guardando = false;
        this.mensaje = this.editando ? 'Juego actualizado.' : 'Juego registrado.';
        this.cancelar();
        this.cargar();
      },
      error: e => {
        this.guardando = false;
        this.error = e.error?.mensaje || 'No fue posible guardar el juego.';
      }
    });
  }

  editar(j: JuegoMesaResponse): void {
    this.form = { ...j };
    this.editando = true;
    this.mensaje = '';
    this.error = '';
  }

  cancelar(): void {
    this.form = this.vacio();
    this.editando = false;
  }

  eliminar(j: JuegoMesaResponse): void {
    if (!confirm(`¿Eliminar el juego ${j.nombre}?`)) return;
    this.service.eliminar(j.numSerie).subscribe({
      next: () => { this.mensaje = 'Juego eliminado.'; this.cargar(); },
      error: e => { this.error = e.error?.mensaje || 'No fue posible eliminar el juego.'; }
    });
  }
}
