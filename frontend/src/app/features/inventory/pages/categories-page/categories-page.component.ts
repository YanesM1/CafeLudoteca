import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Categoria, CategoriaService } from '../../../../core/services/categoria.service';

@Component({
  selector: 'app-categories-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <main class="page">
    <h1>Categorías de juegos</h1>
    <p>Administra las categorías asociadas a los juegos de mesa.</p>

    <section class="panel">
      <h2>{{ editando ? 'Editar categoría' : 'Registrar categoría' }}</h2>
      <form (ngSubmit)="guardar()">
        <label>Número de categoría
          <input type="number" name="numero" [(ngModel)]="form.numCategoria" required [disabled]="editando">
        </label>
        <label>Nombre de categoría
          <input name="categoria" [(ngModel)]="form.categoria" required>
        </label>
        <label>Número de serie del juego
          <input name="serie" [(ngModel)]="form.numSerie" required [disabled]="editando">
        </label>
        <div class="actions">
          <button class="primary" type="submit" [disabled]="guardando">
            {{ guardando ? 'Guardando...' : editando ? 'Actualizar' : 'Registrar' }}
          </button>
          <button type="button" *ngIf="editando" (click)="cancelar()">Cancelar</button>
        </div>
      </form>
    </section>

    <section class="panel">
      <h2>Consultar categorías</h2>
      <div class="search">
        <input name="buscar" [(ngModel)]="busqueda" placeholder="Nombre exacto de categoría">
        <button class="primary" (click)="cargar()">Buscar</button>
      </div>
      <p class="error" *ngIf="error">{{ error }}</p>
      <p class="message" *ngIf="mensaje">{{ mensaje }}</p>
      <p *ngIf="cargando">Consultando...</p>
      <p *ngIf="!cargando && consultado && categorias.length===0">No se encontraron categorías.</p>
      <div class="table-wrap" *ngIf="categorias.length">
        <table>
          <thead><tr><th>Número</th><th>Categoría</th><th>Juego</th><th>Acciones</th></tr></thead>
          <tbody>
            @for (c of categorias; track c.numCategoria + '-' + c.numSerie) {
              <tr>
                <td>{{ c.numCategoria }}</td><td>{{ c.categoria }}</td><td>{{ c.numSerie }}</td>
                <td class="actions">
                  <button (click)="editar(c)">Editar</button>
                  <button class="danger" (click)="eliminar(c)">Eliminar</button>
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
    .page{padding:30px 24px;background:#0b1020;color:#f5f5fa;min-height:80vh}
    h1{font-size:30px}p{color:#aeb5c5}.panel{background:#1b2030;border:1px solid #465267;border-radius:14px;padding:24px;margin:22px 0}
    form{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:16px}
    label{display:grid;gap:8px;color:#c6ccd7}input{box-sizing:border-box;width:100%;padding:13px;background:#101827;color:white;border:1px solid #526078;border-radius:8px}
    button{padding:11px 16px;border-radius:8px;border:1px solid #22d3ee;background:transparent;color:#22d3ee;cursor:pointer}
    .primary{background:#d3b354;color:#171827;border-color:#d3b354}.danger{background:#a32154;color:white;border-color:#a32154}
    .actions,.search{display:flex;gap:10px;align-items:center;flex-wrap:wrap}.actions{grid-column:1/-1}
    table{width:100%;border-collapse:collapse}th,td{text-align:left;padding:13px;border-bottom:1px solid #465267}th{color:#22d3ee}
    .table-wrap{overflow-x:auto}.error{color:#ff7892}.message{color:#22d3ee}
    @media(max-width:750px){form{grid-template-columns:1fr}}
  `]
})
export class CategoriesPageComponent {
  categorias: Categoria[] = [];
  busqueda = '';
  cargando = false;
  guardando = false;
  consultado = false;
  editando = false;
  error = '';
  mensaje = '';
  form: Categoria = { numCategoria: 0, categoria: '', numSerie: '' };

  constructor(private service: CategoriaService) {}

  cargar(): void {
    if (!this.busqueda.trim()) {
      this.error = 'Escribe el nombre de la categoría que deseas consultar.';
      return;
    }
    this.cargando = true;
    this.error = '';
    this.consultado = true;
    this.service.buscar(this.busqueda.trim()).subscribe({
      next: data => { this.categorias = data; this.cargando = false; },
      error: e => { this.error = e.error?.mensaje || 'Error al consultar categorías.'; this.cargando = false; }
    });
  }

  guardar(): void {
    if (!this.form.numCategoria || !this.form.categoria.trim() || !this.form.numSerie.trim()) {
      this.error = 'Completa todos los campos.';
      return;
    }
    this.guardando = true;
    this.error = '';
    const data = { ...this.form, categoria: this.form.categoria.trim(), numSerie: this.form.numSerie.trim() };
    const req = this.editando
      ? this.service.actualizar(data.numCategoria, data.numSerie, data)
      : this.service.crear(data);
    req.subscribe({
      next: () => {
        this.guardando = false;
        this.mensaje = this.editando ? 'Categoría actualizada.' : 'Categoría registrada.';
        this.cancelar();
        if (this.busqueda.trim()) this.cargar();
      },
      error: e => { this.guardando = false; this.error = e.error?.mensaje || 'No se pudo guardar la categoría.'; }
    });
  }

  editar(c: Categoria): void {
    this.form = { ...c };
    this.editando = true;
    this.error = '';
  }

  cancelar(): void {
    this.form = { numCategoria: 0, categoria: '', numSerie: '' };
    this.editando = false;
  }

  eliminar(c: Categoria): void {
    if (!confirm(`¿Eliminar la categoría ${c.categoria} del juego ${c.numSerie}?`)) return;
    this.service.eliminar(c.numCategoria, c.numSerie).subscribe({
      next: () => { this.mensaje = 'Categoría eliminada.'; this.cargar(); },
      error: e => { this.error = e.error?.mensaje || 'No se pudo eliminar.'; }
    });
  }
}
