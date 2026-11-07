import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Categoria, CategoriaService } from '../../../../core/services/categoria.service';
import { Opcion, OpcionesService } from '../../../../core/services/opciones.service';
import { mensajeError } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-categories-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <section class="cl-page">
    <header class="cl-page-header">
      <div>
        <span class="cl-eyebrow">Administración · Juegos</span>
        <h1 class="cl-title">Categorías</h1>
        <p class="cl-subtitle">Categorías asignadas a cada juego (un juego puede tener varias).</p>
      </div>
      <div class="cl-page-header__aside">
        <span class="cl-count"><strong>{{ categorias.length }}</strong>registros</span>
        <button class="btn btn--gold" type="button" (click)="nuevo()">+ Nueva categoría</button>
      </div>
    </header>

    <p class="cl-alert cl-alert--success" *ngIf="mensaje">{{ mensaje }}</p>
    <p class="cl-alert cl-alert--error" *ngIf="error && !formAbierto">{{ error }}</p>

    <section class="cl-card">
      <form class="cl-toolbar" (ngSubmit)="cargar()">
        <input class="form-control" name="buscar" [(ngModel)]="busqueda" placeholder="Nombre o parte del nombre (vacío = todas)">
        <button class="btn btn--gold" type="submit">Buscar</button>
        <button class="btn btn--ghost" type="button" (click)="busqueda='';cargar()">Ver todas</button>
      </form>

      <p class="cl-empty" *ngIf="cargando">Consultando categorías...</p>
      <p class="cl-empty" *ngIf="!cargando && categorias.length === 0">No hay categorías para mostrar.</p>
      <div class="cl-table-wrap" *ngIf="!cargando && categorias.length">
        <table class="cl-table">
          <thead><tr><th>Número</th><th>Categoría</th><th>Juego</th><th class="cl-table__actions-head">Acciones</th></tr></thead>
          <tbody>
            @for (c of categorias; track c.numCategoria + '-' + c.numSerie) {
              <tr>
                <td>{{ c.numCategoria }}</td><td>{{ c.categoria }}</td><td>{{ nombreJuego(c.numSerie) }} <span class="cl-muted">· {{ c.numSerie }}</span></td>
                <td>
                  <div class="cl-table__actions">
                    <button class="btn btn--ghost btn--sm" type="button" (click)="editar(c)">Editar</button>
                    <button class="btn btn--danger btn--sm" type="button" (click)="eliminar(c)">Eliminar</button>
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
        <h2 class="cl-card__title">{{ editando ? 'Editar categoría' : 'Nueva categoría' }}</h2>
        <p class="cl-alert cl-alert--error" *ngIf="error">{{ error }}</p>
        <form class="cl-form-grid" (ngSubmit)="guardar()">
          <label class="cl-field cl-field--full">Juego *
            <select class="form-control" name="serie" [(ngModel)]="form.numSerie" required [disabled]="editando">
              <option value="" disabled>Selecciona un juego</option>
              @for (j of juegos; track j.value) { <option [value]="j.value">{{ j.label }}</option> }
            </select>
          </label>
          <label class="cl-field cl-field--full">Categoría *
            <input class="form-control" name="categoria" maxlength="50" [(ngModel)]="form.categoria" required
              list="categorias-existentes" placeholder="Elige una existente o escribe una nueva">
            <datalist id="categorias-existentes">
              @for (n of nombresCategoria; track n) { <option [value]="n"></option> }
            </datalist>
            <span class="cl-field__hint" *ngIf="!editando">El número de categoría se asigna automáticamente.</span>
          </label>
          <div class="cl-form-actions">
            <button class="btn btn--ghost" type="button" (click)="cancelar()">Cancelar</button>
            <button class="btn btn--gold" type="submit" [disabled]="guardando">{{ guardando ? 'Guardando...' : 'Guardar categoría' }}</button>
          </div>
        </form>
      </section>
    </div>
  </section>

  `,
  styles: [':host { display: block; }']
})
export class CategoriesPageComponent implements OnInit {
  /** Juegos para la lista desplegable (en vez de escribir el número de serie). */
  private readonly opciones = inject(OpcionesService);
  juegos: Opcion[] = [];
  private cargarJuegos(): void {
    this.opciones.juegos().subscribe(j => this.juegos = j);
  }
  /** Nombres de categoría ya usados, para elegir sin escribir de nuevo. */
  get nombresCategoria(): string[] {
    return [...new Set(this.categorias.map(c => c.categoria))].sort();
  }
  nombreJuego(serie: string): string {
    const j = this.juegos.find(o => o.value === serie);
    return j ? j.label.split(' · ')[0] : serie;
  }

  categorias: Categoria[] = [];
  busqueda = '';
  cargando = false;
  guardando = false;
  editando = false;
  formAbierto = false;
  error = '';
  mensaje = '';
  form: Categoria = { numCategoria: 0, categoria: '', numSerie: '' };

  constructor(private service: CategoriaService) {}

  ngOnInit(): void {this.cargarJuegos(); this.cargar(); }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.service.listar(this.busqueda).subscribe({
      next: data => { this.categorias = data; this.cargando = false; },
      error: e => { this.error = mensajeError(e, 'Error al consultar categorías.'); this.cargando = false; }
    });
  }

  guardar(): void {
    if (!this.form.categoria.trim() || !this.form.numSerie.trim()) {
      this.error = 'Selecciona el juego y escribe la categoría.';
      return;
    }
    this.guardando = true;
    this.error = '';
    this.mensaje = '';
    const editando = this.editando;
    const data = { ...this.form, categoria: this.form.categoria.trim(), numSerie: this.form.numSerie.trim() };
    const req = editando
      ? this.service.actualizar(data.numCategoria, data.numSerie, data)
      : this.service.crear(data);
    req.subscribe({
      next: () => {
        this.guardando = false;
        this.mensaje = editando ? 'Categoría actualizada.' : 'Categoría registrada.';
        this.cancelar();
        this.cargar();
      },
      error: e => { this.guardando = false; this.error = mensajeError(e, 'No se pudo guardar la categoría.'); }
    });
  }

  nuevo(): void {
    this.cancelar();
    this.formAbierto = true;
    this.mensaje = '';
    this.error = '';
  }

  editar(c: Categoria): void {
    this.form = { ...c };
    this.editando = true;
    this.formAbierto = true;
    this.error = '';
  }

  cancelar(): void {
    this.form = { numCategoria: 0, categoria: '', numSerie: '' };
    this.editando = false;
    this.formAbierto = false;
  }

  eliminar(c: Categoria): void {
    if (!confirm(`¿Eliminar la categoría ${c.categoria} del juego ${c.numSerie}?`)) return;
    this.service.eliminar(c.numCategoria, c.numSerie).subscribe({
      next: () => { this.mensaje = 'Categoría eliminada.'; this.cargar(); },
      error: e => { this.error = mensajeError(e, 'No se pudo eliminar.'); }
    });
  }
}
