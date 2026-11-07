import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  ESTADOS_UNIDAD,
  UnidadDisponibilidad,
  UnidadJuego,
  UnidadJuegoService,
  etiquetaEstadoUnidad,
  normalizarEstadoUnidad
} from '../../../../core/services/unidad-juego.service';
import { Opcion, OpcionesService } from '../../../../core/services/opciones.service';
import { mensajeError } from '../../../../core/utils/http-error';

@Component({
  selector:'app-units-page',
  standalone:true,
  imports:[CommonModule,FormsModule],
  template: `
  <section class="cl-page">
    <header class="cl-page-header">
      <div>
        <span class="cl-eyebrow">Administración · Juegos</span>
        <h1 class="cl-title">Unidades físicas</h1>
        <p class="cl-subtitle">Cada copia física de un juego y su estado. "En uso en mesa" y "Prestado a domicilio" los asignan los préstamos y la devolución.</p>
      </div>
      <div class="cl-page-header__aside">
        <span class="cl-count"><strong>{{ unidadesFiltradas.length }}</strong>registros</span>
        <button class="btn btn--gold" type="button" (click)="nuevo()">+ Nueva unidad</button>
      </div>
    </header>

    <p class="cl-alert cl-alert--success" *ngIf="message">{{ message }}</p>
    <p class="cl-alert cl-alert--error" *ngIf="error && !formAbierto">{{ error }}</p>

    <section class="cl-card">
      <form class="cl-toolbar" (ngSubmit)="load()">
        <select class="form-control" name="filtroSerie" [(ngModel)]="filtroSerie" aria-label="Juego">
          <option value="">Todos los juegos</option>
          @for (j of juegos; track j.value) { <option [value]="j.value">{{ j.label }}</option> }
        </select>
        <select class="form-control" [(ngModel)]="filtroEstado" name="filtroEstado" aria-label="Estado">
          <option value="">Todos los estados</option>
          @for (e of estados; track e.value) {
            <option [value]="e.value">{{ e.label }}</option>
          }
        </select>
        <button class="btn btn--gold" type="submit">Buscar</button>
        <button class="btn btn--ghost" type="button" (click)="filtroSerie='';filtroEstado='';load()">Ver todas</button>
      </form>

      <p class="cl-empty" *ngIf="loading">Consultando unidades...</p>
      <p class="cl-empty" *ngIf="!loading && !unidadesFiltradas.length">No hay unidades para el filtro seleccionado.</p>
      <div class="cl-table-wrap" *ngIf="!loading && unidadesFiltradas.length">
        <table class="cl-table">
          <thead><tr><th>Código</th><th>Juego</th><th>Estado</th><th>Observaciones</th><th class="cl-table__actions-head">Acciones</th></tr></thead>
          <tbody>
            @for (u of unidadesFiltradas; track u.numSerie + '-' + u.codigo) {
              <tr>
                <td>{{ u.codigo }}</td>
                <td>{{ nombreJuego(u.numSerie) }} <span class="cl-muted">· {{ u.numSerie }}</span></td>
                <td><span class="badge" [ngClass]="claseEstado(u.estado)">{{ etiqueta(u.estado) }}</span></td>
                <td>{{ u.observaciones || '—' }}</td>
                <td>
                  <div class="cl-table__actions">
                    <button class="btn btn--ghost btn--sm" type="button" (click)="edit(u)">Editar</button>
                    <button class="btn btn--danger btn--sm" type="button" (click)="remove(u)" [disabled]="esDePrestamo(normalizar(u.estado))">Eliminar</button>
                  </div>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </section>

    <section class="cl-card">
      <div class="cl-card__header">
        <h2 class="cl-card__title">Disponibilidad por juego</h2>
        <span class="cl-card__hint">Unidades en estado Disponible</span>
      </div>
      <p class="cl-empty" *ngIf="!availability.length">Sin datos de disponibilidad.</p>
      <div class="cl-table-wrap" *ngIf="availability.length">
        <table class="cl-table">
          <thead><tr><th>Juego</th><th>Unidades disponibles</th></tr></thead>
          <tbody>
            @for (u of availability; track u.numSerie) {
              <tr><td>{{ nombreJuego(u.numSerie) }} <span class="cl-muted">· {{ u.numSerie }}</span></td><td>{{ u.unidadesDisponibles }}</td></tr>
            }
          </tbody>
        </table>
      </div>
    </section>

    <div class="cl-modal-overlay" *ngIf="formAbierto" (click)="cancel()">
      <section class="cl-card cl-modal" role="dialog" aria-modal="true" (click)="$event.stopPropagation()">
        <h2 class="cl-card__title">{{ editing ? 'Editar unidad' : 'Nueva unidad' }}</h2>
        <p class="cl-alert cl-alert--error" *ngIf="error">{{ error }}</p>
        <form class="cl-form-grid" (ngSubmit)="save()">
          <label class="cl-field cl-field--full">Juego *
            <select class="form-control" name="serie" [(ngModel)]="form.numSerie" required [disabled]="editing">
              <option value="" disabled>Selecciona un juego</option>
              @for (j of juegos; track j.value) { <option [value]="j.value">{{ j.label }}</option> }
            </select>
          </label>
          <label class="cl-field">Código {{ editing ? '' : '(opcional)' }}
            <input class="form-control" name="codigo" maxlength="30" [(ngModel)]="form.codigo" [disabled]="editing"
              [placeholder]="codigoSugerido || 'Se asigna automáticamente'">
            <span class="cl-field__hint" *ngIf="!editing">Si lo dejas vacío se asigna {{ codigoSugerido || 'automáticamente' }}.</span>
          </label>
          <label class="cl-field">Estado *
            <select class="form-control" name="estado" [(ngModel)]="form.estado" required [disabled]="esDePrestamo(form.estado) && editing">
              @for (e of estadosFormulario; track e.value) {
                <option [value]="e.value" [disabled]="esDePrestamo(e.value) && form.estado!==e.value">{{ e.label }}</option>
              }
            </select>
          </label>
          <label class="cl-field">Observaciones
            <input class="form-control" name="observaciones" maxlength="500" [(ngModel)]="form.observaciones">
          </label>
          <div class="cl-form-actions">
            <button class="btn btn--ghost" type="button" (click)="cancel()">Cancelar</button>
            <button class="btn btn--gold" type="submit" [disabled]="saving">{{ saving ? 'Guardando...' : 'Guardar unidad' }}</button>
          </div>
        </form>
      </section>
    </div>
  </section>

  `,
  styles: [':host { display: block; }']
})
export class UnitsPageComponent implements OnInit {
  /** Juegos para la lista desplegable (en vez de escribir el número de serie). */
  private readonly opciones = inject(OpcionesService);
  juegos: Opcion[] = [];
  private cargarJuegos(): void {
    this.opciones.juegos().subscribe(j => this.juegos = j);
  }
  /** Al crear: Disponible, Mantenimiento o Con faltantes. "Prestado" solo lo asignan los préstamos. */
  get estadosFormulario(){
    if(this.editing) return this.estados;
    return this.estados.filter(e=>['DISPONIBLE','MANTENIMIENTO','FALTANTES'].includes(e.value));
  }

  /** Serie con la que se cargó la tabla (vacío = todas). */
  private serieCargada='';

  /** Código que asignará el backend si se deja vacío (UJ-CATAN-002). */
  get codigoSugerido():string{
    const serie=this.form.numSerie;
    if(!serie||(this.serieCargada&&this.serieCargada!==serie)) return '';
    const base=(serie.split('-')[0]||'JUEGO').toUpperCase().replace(/[^A-Z0-9]/g,'').substring(0,20)||'JUEGO';
    const usados=new Set(this.unidades.filter(u=>u.numSerie===serie).map(u=>u.codigo));
    let n=this.unidades.filter(u=>u.numSerie===serie).length+1;
    let codigo='';
    do { codigo=`UJ-${base}-${String(n++).padStart(3,'0')}`; } while(usados.has(codigo));
    return codigo;
  }

  nombreJuego(serie: string): string {
    const j = this.juegos.find(o => o.value === serie);
    return j ? j.label.split(' · ')[0] : serie;
  }

  readonly estados = ESTADOS_UNIDAD;
  form:UnidadJuego=this.vacia();
  unidades:UnidadJuego[]=[];
  availability:UnidadDisponibilidad[]=[];
  filtroSerie='';
  filtroEstado='';
  editing=false;
  formAbierto=false;
  saving=false;
  loading=false;
  error='';
  message='';

  constructor(private service:UnidadJuegoService){}

  ngOnInit():void{this.cargarJuegos();this.load();}

  private vacia():UnidadJuego{
    return {codigo:'',estado:'DISPONIBLE',observaciones:'',numSerie:''};
  }

  get unidadesFiltradas():UnidadJuego[]{
    return this.filtroEstado
      ? this.unidades.filter(u=>this.normalizar(u.estado)===this.filtroEstado)
      : this.unidades;
  }

  /** Prestado y En mesa los asignan los préstamos (externo e interno). */
  esDePrestamo(estado:string):boolean{return estado==='PRESTADO'||estado==='EN_MESA';}

  normalizar(estado:string):string{return normalizarEstadoUnidad(estado);}
  etiqueta(estado:string):string{return etiquetaEstadoUnidad(estado);}

  claseEstado(estado:string):string{
    switch(this.normalizar(estado)){
      case 'DISPONIBLE': return 'badge--success';
      case 'PRESTADO': case 'EN_MESA': return 'badge--warning';
      case 'MANTENIMIENTO': case 'FALTANTES': return 'badge--danger';
      default: return 'badge--info';
    }
  }

  nuevo():void{this.cancel();this.formAbierto=true;this.error='';this.message='';}

  load():void{
    this.loading=true;this.error='';
    const serie=this.filtroSerie;
    this.service.listar(serie).subscribe({
      next:data=>{this.unidades=data;this.serieCargada=serie;this.loading=false;},
      error:e=>{this.loading=false;this.error=mensajeError(e,'No fue posible consultar las unidades.');}
    });
    this.service.consultarDisponibilidad().subscribe({
      next:data=>this.availability=data,
      error:e=>this.error=mensajeError(e,'Error al consultar disponibilidad.')
    });
  }

  save():void{
    if(!this.form.numSerie.trim()||!this.form.estado){
      this.error='Selecciona el juego y el estado.';return;
    }
    this.saving=true;this.error='';this.message='';
    const editando=this.editing;
    const data={...this.form,codigo:this.form.codigo.trim(),numSerie:this.form.numSerie.trim()};
    const req=editando
      ? this.service.actualizar(data.codigo,data.numSerie,data)
      : this.service.crear(data);
    req.subscribe({
      next:()=>{
        this.saving=false;
        this.message=editando?'Unidad actualizada.':'Unidad registrada.';
        this.cancel();this.load();
      },
      error:e=>{this.saving=false;this.error=mensajeError(e,'No se pudo guardar la unidad.');}
    });
  }

  edit(u:UnidadJuego):void{
    this.form={...u,estado:this.normalizar(u.estado),observaciones:u.observaciones??''};
    this.editing=true;this.formAbierto=true;this.error='';this.message='';
  }

  cancel():void{this.form=this.vacia();this.editing=false;this.formAbierto=false;}

  remove(u:UnidadJuego):void{
    if(!confirm(`¿Eliminar la unidad ${u.codigo} de ${u.numSerie}?`))return;
    this.error='';
    this.service.eliminar(u.codigo,u.numSerie).subscribe({
      next:()=>{this.message='Unidad eliminada.';this.load();},
      error:e=>this.error=mensajeError(e,'No se pudo eliminar.')
    });
  }
}
