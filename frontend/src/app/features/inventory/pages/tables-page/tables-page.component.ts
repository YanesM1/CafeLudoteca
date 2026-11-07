import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Mesa, MesaService } from '../../../../core/services/mesa.service';

import { mensajeError } from '../../../../core/utils/http-error';
@Component({
 selector:'app-tables-page',
 standalone:true,
 imports:[CommonModule,FormsModule],
  template: `
  <section class="cl-page">
    <header class="cl-page-header">
      <div>
        <span class="cl-eyebrow">Administración · Mesas</span>
        <h1 class="cl-title">Mesas</h1>
        <p class="cl-subtitle">Mesas temáticas disponibles para reservas.</p>
      </div>
      <div class="cl-page-header__aside">
        <span class="cl-count"><strong>{{ mesasFiltradas.length }}</strong>registros</span>
        <button class="btn btn--gold" type="button" (click)="nuevo()">+ Nueva mesa</button>
      </div>
    </header>

    <p class="cl-alert cl-alert--success" *ngIf="message">{{ message }}</p>
    <p class="cl-alert cl-alert--error" *ngIf="error && !formAbierto">{{ error }}</p>

    <section class="cl-card">
      <form class="cl-toolbar" (ngSubmit)="filtroAplicado=filtro.trim()">
        <input class="form-control" name="filtroMesa" [(ngModel)]="filtro" placeholder="Buscar por número, temática o ubicación">
        <button class="btn btn--gold" type="submit">Buscar</button>
        <button class="btn btn--ghost" type="button" (click)="filtro='';filtroAplicado='';load()">Ver todas</button>
      </form>
      <p class="cl-empty" *ngIf="loading">Consultando mesas...</p>
      <p class="cl-empty" *ngIf="!loading && !mesasFiltradas.length">No hay mesas para mostrar.</p>
      <div class="cl-table-wrap" *ngIf="!loading && mesasFiltradas.length">
        <table class="cl-table">
          <thead><tr><th>Mesa</th><th>Capacidad</th><th>Temática</th><th>Ubicación</th><th class="cl-table__actions-head">Acciones</th></tr></thead>
          <tbody>
            @for (m of mesasFiltradas; track m.idMesa) {
              <tr>
                <td>{{ m.idMesa }}</td><td>{{ m.capacidad }} personas</td><td>{{ m.tematica }}</td><td>{{ m.ubicacion }}</td>
                <td>
                  <div class="cl-table__actions">
                    <button class="btn btn--ghost btn--sm" type="button" (click)="edit(m)">Editar</button>
                    <button class="btn btn--danger btn--sm" type="button" (click)="remove(m)">Eliminar</button>
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
        <h2 class="cl-card__title">{{ editing ? 'Editar mesa ' + form.idMesa : 'Nueva mesa' }}</h2>
        <p class="cl-alert cl-alert--error" *ngIf="error">{{ error }}</p>
        <form class="cl-form-grid" (ngSubmit)="save()">
          <label class="cl-field">Número de mesa *
            <input class="form-control" name="id" [(ngModel)]="form.idMesa" [disabled]="editing" required inputmode="numeric" placeholder="Ej: 1">
            <span class="cl-field__hint" *ngIf="!editing">Se sugiere el siguiente número libre.</span>
          </label>
          <label class="cl-field">Capacidad (personas) *
            <input class="form-control" type="number" name="capacidad" [(ngModel)]="form.capacidad" min="1" required>
          </label>
          <label class="cl-field">Temática *
            <input class="form-control" name="tematica" [(ngModel)]="form.tematica" required list="lista-tematicas" placeholder="Ej: Estrategia">
            <datalist id="lista-tematicas">@for (t of valores('tematica'); track t) { <option [value]="t"></option> }</datalist>
          </label>
          <label class="cl-field">Ubicación *
            <input class="form-control" name="ubicacion" [(ngModel)]="form.ubicacion" required list="lista-ubicaciones" placeholder="Ej: Zona A">
            <datalist id="lista-ubicaciones">@for (u of valores('ubicacion'); track u) { <option [value]="u"></option> }</datalist>
          </label>
          <div class="cl-form-actions">
            <button class="btn btn--ghost" type="button" (click)="cancel()">Cancelar</button>
            <button class="btn btn--gold" type="submit" [disabled]="saving">{{ saving ? 'Guardando...' : 'Guardar mesa' }}</button>
          </div>
        </form>
      </section>
    </div>
  </section>

  `,
  styles: [':host { display: block; }']
})
export class TablesPageComponent implements OnInit {
  filtro='';
  filtroAplicado='';

  get mesasFiltradas(){
    const t=this.filtroAplicado.toLowerCase();
    if(!t) return this.mesas;
    return this.mesas.filter(m=>[m.idMesa,m.tematica,m.ubicacion]
      .some(v=>String(v??'').toLowerCase().includes(t)));
  }

 mesas:Mesa[]=[];
 form:Mesa={idMesa:'',capacidad:1,tematica:'',ubicacion:''};
 editing=false;formAbierto=false;saving=false;loading=false;error='';message='';
 constructor(private service:MesaService){}
 ngOnInit():void{this.load();}
 load():void{
  this.loading=true;this.error='';
  this.service.listar().subscribe({
   next:data=>{this.mesas=data;this.loading=false;},
   error:e=>{this.error=mensajeError(e,'No fue posible consultar mesas.');this.loading=false;}
  });
 }
 save():void{
  const id=String(this.form.idMesa??'').trim();
  if(!id||!this.form.tematica.trim()||!this.form.ubicacion.trim()||this.form.capacidad<1){
   this.error='Completa los datos de la mesa.';return;
  }
  if(!/^\d+$/.test(id)){
   this.error='El número de mesa debe ser numérico (las reservas lo guardan como número).';return;
  }
  this.saving=true;this.error='';this.message='';
  const editando=this.editing;
  const data={...this.form,idMesa:id};
  const req=editando?this.service.actualizar(data.idMesa,data):this.service.crear(data);
  req.subscribe({
   next:()=>{this.saving=false;this.message=editando?'Mesa actualizada.':'Mesa registrada.';this.cancel();this.load();},
   error:e=>{this.saving=false;this.error=mensajeError(e,'No fue posible guardar la mesa.');}
  });
 }
 /** Temáticas o ubicaciones ya usadas, para elegirlas sin escribir de nuevo. */
 valores(campo:'tematica'|'ubicacion'):string[]{
  return [...new Set(this.mesas.map(m=>m[campo]).filter(Boolean))].sort();
 }
 nuevo():void{
  this.cancel();
  // Se sugiere el siguiente número libre; se puede cambiar si la mesa física tiene otro
  const numeros=this.mesas.map(m=>Number(m.idMesa)).filter(n=>Number.isFinite(n));
  this.form.idMesa=String((numeros.length?Math.max(...numeros):0)+1);
  this.formAbierto=true;this.error='';this.message='';
 }
 edit(m:Mesa):void{this.form={...m,idMesa:String(m.idMesa)};this.editing=true;this.formAbierto=true;this.error='';this.message='';}
 cancel():void{this.form={idMesa:'',capacidad:1,tematica:'',ubicacion:''};this.editing=false;this.formAbierto=false;}
 remove(m:Mesa):void{
  if(!confirm(`¿Eliminar mesa ${m.idMesa}?`))return;
  this.service.eliminar(m.idMesa).subscribe({
   next:()=>{this.message='Mesa eliminada.';this.load();},
   error:e=>this.error=mensajeError(e,'No fue posible eliminar la mesa.')
  });
 }
}
