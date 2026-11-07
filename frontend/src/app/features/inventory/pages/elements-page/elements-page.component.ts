import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ElementoJuego, ElementoJuegoService } from '../../../../core/services/elemento-juego.service';
import { Opcion, OpcionesService } from '../../../../core/services/opciones.service';
import { mensajeError } from '../../../../core/utils/http-error';

@Component({
  selector:'app-elements-page',
  standalone:true,
  imports:[CommonModule,FormsModule],
  template: `
  <section class="cl-page">
    <header class="cl-page-header">
      <div>
        <span class="cl-eyebrow">Administración · Juegos</span>
        <h1 class="cl-title">Elementos de juego</h1>
        <p class="cl-subtitle">Componentes que trae cada juego (tablero, fichas, dados...), para verificar devoluciones.</p>
      </div>
      <div class="cl-page-header__aside">
        <span class="cl-count"><strong>{{ items.length }}</strong>registros</span>
        <button class="btn btn--gold" type="button" (click)="nuevo()">+ Nuevo elemento</button>
      </div>
    </header>

    <p class="cl-alert cl-alert--success" *ngIf="message">{{ message }}</p>
    <p class="cl-alert cl-alert--error" *ngIf="error && !formAbierto">{{ error }}</p>

    <section class="cl-card">
      <form class="cl-toolbar" (ngSubmit)="load()">
        <select class="form-control" name="filtro" [(ngModel)]="filtroSerie" aria-label="Juego">
          <option value="">Todos los juegos</option>
          @for (j of juegos; track j.value) { <option [value]="j.value">{{ j.label }}</option> }
        </select>
        <button class="btn btn--gold" type="submit">Buscar</button>
        <button class="btn btn--ghost" type="button" (click)="filtroSerie='';load()">Ver todos</button>
      </form>

      <p class="cl-empty" *ngIf="loading">Consultando elementos...</p>
      <p class="cl-empty" *ngIf="!loading && !items.length">No hay elementos para mostrar.</p>
      <div class="cl-table-wrap" *ngIf="!loading && items.length">
        <table class="cl-table">
          <thead><tr><th>ID</th><th>Elemento</th><th>Juego</th><th class="cl-table__actions-head">Acciones</th></tr></thead>
          <tbody>
            @for (item of items; track item.idElementoJuego) {
              <tr>
                <td>{{ item.idElementoJuego }}</td><td>{{ item.elementoJuego }}</td><td>{{ nombreJuego(item.numSerie) }} <span class="cl-muted">· {{ item.numSerie }}</span></td>
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
        <h2 class="cl-card__title">{{ editing ? 'Editar elemento #' + form.idElementoJuego : 'Nuevo elemento' }}</h2>
        <p class="cl-alert cl-alert--error" *ngIf="error">{{ error }}</p>
        <form class="cl-form-grid" (ngSubmit)="save()">
          <label class="cl-field">Descripción del elemento *
            <input class="form-control" name="elemento" maxlength="100" [(ngModel)]="form.elementoJuego" required placeholder="Ej: Dado de 6 caras">
          </label>
          <label class="cl-field">Juego *
            <select class="form-control" name="serie" [(ngModel)]="form.numSerie" required>
              <option value="" disabled>Selecciona un juego</option>
              @for (j of juegos; track j.value) { <option [value]="j.value">{{ j.label }}</option> }
            </select>
          </label>
          <div class="cl-form-actions">
            <button class="btn btn--ghost" type="button" (click)="cancel()">Cancelar</button>
            <button class="btn btn--gold" type="submit" [disabled]="saving">{{ saving ? 'Guardando...' : 'Guardar elemento' }}</button>
          </div>
        </form>
      </section>
    </div>
  </section>

  `,
  styles: [':host { display: block; }']
})
export class ElementsPageComponent implements OnInit {
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

  form:ElementoJuego={elementoJuego:'',numSerie:''};
  items:ElementoJuego[]=[];
  filtroSerie='';
  editing=false;
  formAbierto=false;
  saving=false;
  loading=false;
  error='';
  message='';

  constructor(private service:ElementoJuegoService){}

  ngOnInit():void{this.cargarJuegos();this.load();}

  load():void{
    this.loading=true;this.error='';
    this.service.listar(this.filtroSerie).subscribe({
      next:data=>{this.items=data;this.loading=false;},
      error:e=>{this.loading=false;this.error=mensajeError(e,'No fue posible consultar los elementos.');}
    });
  }

  save():void{
    if(!this.form.elementoJuego.trim()||!this.form.numSerie.trim()){
      this.error='Completa el elemento y el número de serie.';return;
    }
    this.saving=true;this.error='';this.message='';
    const data={...this.form,elementoJuego:this.form.elementoJuego.trim(),numSerie:this.form.numSerie.trim()};
    const editando=this.editing;
    const req=editando
      ? this.service.actualizar(this.form.idElementoJuego!,data)
      : this.service.crear(data);
    req.subscribe({
      next:()=>{
        this.saving=false;
        this.message=editando?'Elemento actualizado.':'Elemento registrado.';
        this.cancel();
        this.load();
      },
      error:e=>{this.saving=false;this.error=mensajeError(e,'No se pudo guardar el elemento.');}
    });
  }

  nuevo():void{
    this.cancel();this.formAbierto=true;this.error='';this.message='';
  }

  edit(item:ElementoJuego):void{
    this.form={...item};this.editing=true;this.formAbierto=true;this.error='';this.message='';
  }

  cancel():void{
    this.form={elementoJuego:'',numSerie:''};this.editing=false;this.formAbierto=false;
  }

  remove(item:ElementoJuego):void{
    if(!item.idElementoJuego||!confirm(`¿Eliminar "${item.elementoJuego}"?`))return;
    this.error='';
    this.service.eliminar(item.idElementoJuego).subscribe({
      next:()=>{this.message='Elemento eliminado.';this.load();},
      error:e=>this.error=mensajeError(e,'No se pudo eliminar.')
    });
  }
}
