import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Expansion, ExpansionService } from '../../../../core/services/expansion.service';
import { Opcion, OpcionesService } from '../../../../core/services/opciones.service';
import { mensajeError } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-expansions-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <section class="cl-page">
    <header class="cl-page-header">
      <div>
        <span class="cl-eyebrow">Administración · Juegos</span>
        <h1 class="cl-title">Expansiones</h1>
        <p class="cl-subtitle">Contenido adicional asociado a un juego principal.</p>
      </div>
      <div class="cl-page-header__aside">
        <span class="cl-count"><strong>{{ items.length }}</strong>registros</span>
        <button class="btn btn--gold" type="button" (click)="nuevo()">+ Nueva expansión</button>
      </div>
    </header>

    <p class="cl-alert cl-alert--success" *ngIf="message">{{ message }}</p>
    <p class="cl-alert cl-alert--error" *ngIf="error && !formAbierto">{{ error }}</p>

    <section class="cl-card">
      <form class="cl-toolbar" (ngSubmit)="load()">
        <input class="form-control" name="search" [(ngModel)]="search" placeholder="Nombre o parte del nombre (vacío = todas)">
        <button class="btn btn--gold" type="submit">Buscar</button>
        <button class="btn btn--ghost" type="button" (click)="search='';load()">Ver todas</button>
      </form>

      <p class="cl-empty" *ngIf="!items.length">No hay expansiones para mostrar.</p>
      <div class="cl-table-wrap" *ngIf="items.length">
        <table class="cl-table">
          <thead><tr><th>ID</th><th>Nombre</th><th>Juego</th><th>Descripción</th><th>Contenido adicional</th><th class="cl-table__actions-head">Acciones</th></tr></thead>
          <tbody>
            @for (item of items; track item.idExpansion) {
              <tr>
                <td>{{ item.idExpansion }}</td><td>{{ item.nombre }}</td><td>{{ nombreJuego(item.numSerie) }} <span class="cl-muted">· {{ item.numSerie }}</span></td>
                <td>{{ item.descripcion || '—' }}</td><td>{{ item.contenidoAdicional || '—' }}</td>
                <td>
                  <div class="cl-table__actions">
                    <button class="btn btn--ghost btn--sm" type="button" (click)="edit(item)">Editar</button>
                    <button class="btn btn--danger btn--sm" type="button" (click)="remove(item)">Eliminar</button>
                  </div>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </section>

    <div class="cl-modal-overlay" *ngIf="formAbierto" (click)="cancel()">
      <section class="cl-card cl-modal" role="dialog" aria-modal="true" (click)="$event.stopPropagation()">
        <h2 class="cl-card__title">{{ editing ? 'Editar expansión' : 'Nueva expansión' }}</h2>
        <p class="cl-alert cl-alert--error" *ngIf="error">{{ error }}</p>
        <form class="cl-form-grid" (ngSubmit)="save()">
          <label class="cl-field">Nombre *
            <input class="form-control" name="nombre" [(ngModel)]="form.nombre" required>
          </label>
          <label class="cl-field">Juego *
            <select class="form-control" name="serie" [(ngModel)]="form.numSerie" required>
              <option value="" disabled>Selecciona un juego</option>
              @for (j of juegos; track j.value) { <option [value]="j.value">{{ j.label }}</option> }
            </select>
          </label>
          <label class="cl-field cl-field--full">Descripción *
            <input class="form-control" name="descripcion" [(ngModel)]="form.descripcion" required>
          </label>
          <label class="cl-field cl-field--full">Contenido adicional *
            <input class="form-control" name="contenido" [(ngModel)]="form.contenidoAdicional" required placeholder="Ej: 20 cartas, 4 fichas">
          </label>
          <div class="cl-form-actions">
            <button class="btn btn--ghost" type="button" (click)="cancel()">Cancelar</button>
            <button class="btn btn--gold" type="submit" [disabled]="saving">{{ saving ? 'Guardando...' : 'Guardar expansión' }}</button>
          </div>
        </form>
      </section>
    </div>
  </section>

  `,
  styles: [':host { display: block; }']
})
export class ExpansionsPageComponent implements OnInit {
  /** Juegos para la lista desplegable (en vez de escribir el número de serie). */
  private readonly opciones = inject(OpcionesService);
  juegos: Opcion[] = [];
  private cargarJuegos(): void {
    this.opciones.juegos().subscribe(j => this.juegos = j);
  }
  nombreJuego(serie: string): string {
    const j = this.juegos.find(o => o.value === serie);
    return j ? j.label.split(' · ')[0] : serie;
  }

  items: Expansion[] = [];
  form: Expansion = this.empty();
  search = '';
  editing = false;
  formAbierto = false;
  saving = false;
  error = '';
  message = '';

  constructor(private service: ExpansionService) {}

  ngOnInit(): void {this.cargarJuegos(); this.load(); }

  private empty(): Expansion {
    return { nombre:'', descripcion:'', contenidoAdicional:'', numSerie:'' };
  }

  load(): void {
    this.error='';
    this.service.listar(this.search).subscribe({
      next: data => this.items=data,
      error: e => this.error=mensajeError(e, 'Error al consultar expansiones.')
    });
  }

  save(): void {
    if (!this.form.nombre.trim() || !this.form.numSerie.trim()
        || !(this.form.descripcion||'').trim() || !(this.form.contenidoAdicional||'').trim()) {
      this.error='Completa el nombre, el juego, la descripción y el contenido adicional.'; return;
    }
    this.saving=true; this.error=''; this.message='';
    const data={...this.form};
    const editando=this.editing;
    const request=editando
      ? this.service.actualizar(data.idExpansion!,data)
      : this.service.crear(data);
    request.subscribe({
      next: () => {
        this.saving=false;
        this.message=editando?'Expansión actualizada.':'Expansión registrada.';
        this.cancel();
        this.load();
      },
      error: e => {this.saving=false;this.error=mensajeError(e, 'No se pudo guardar.');}
    });
  }

  nuevo(): void { this.cancel(); this.formAbierto=true; this.error=''; this.message=''; }

  edit(item: Expansion): void { this.form={...item}; this.editing=true; this.formAbierto=true; this.error=''; this.message=''; }

  cancel(): void { this.form=this.empty(); this.editing=false; this.formAbierto=false; }

  remove(item: Expansion): void {
    if(!item.idExpansion || !confirm(`¿Eliminar ${item.nombre}?`)) return;
    this.service.eliminar(item.idExpansion).subscribe({
      next:()=>{this.message='Expansión eliminada.';this.load();},
      error:e=>this.error=mensajeError(e, 'No se pudo eliminar.')
    });
  }
}
