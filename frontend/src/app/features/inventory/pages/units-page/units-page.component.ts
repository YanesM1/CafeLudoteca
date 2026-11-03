import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UnidadJuego, UnidadJuegoService, UnidadDisponibilidad } from '../../../../core/services/unidad-juego.service';

@Component({
  selector:'app-units-page',
  standalone:true,
  imports:[CommonModule,FormsModule],
  template:`
  <main class="page">
    <h1>Unidades físicas</h1>
    <p>Administra los ejemplares físicos y su disponibilidad.</p>
    <section class="panel">
      <h2>{{editing?'Editar unidad':'Registrar unidad'}}</h2>
      <form (ngSubmit)="save()">
        <label>Código único
          <input name="codigo" [(ngModel)]="form.codigo" [disabled]="editing" required>
        </label>
        <label>Número de serie del juego
          <input name="serie" [(ngModel)]="form.numSerie" [disabled]="editing" required>
        </label>
        <label>Estado
          <select name="estado" [(ngModel)]="form.estado" required>
            <option value="">Seleccionar</option>
            <option value="Disponible">Disponible</option>
            <option value="Prestado">Prestado</option>
            <option value="Mantenimiento">Mantenimiento</option>
            <option value="Dañado">Dañado</option>
          </select>
        </label>
        <label>Observaciones
          <input name="observaciones" [(ngModel)]="form.observaciones">
        </label>
        <div class="actions">
          <button class="primary" [disabled]="saving">{{saving?'Guardando...':editing?'Actualizar':'Registrar'}}</button>
          <button type="button" *ngIf="editing" (click)="cancel()">Cancelar</button>
        </div>
      </form>
    </section>
    <section class="panel">
      <h2>Consultar unidad</h2>
      <div class="search">
        <input [(ngModel)]="searchCodigo" placeholder="Código">
        <input [(ngModel)]="searchSerie" placeholder="Número de serie">
        <button class="primary" (click)="loadOne()">Consultar</button>
      </div>
      <p class="error">{{error}}</p>
      <p class="message">{{message}}</p>
      <table *ngIf="unit">
        <thead><tr><th>Código</th><th>Juego</th><th>Estado</th><th>Observaciones</th><th>Acciones</th></tr></thead>
        <tbody><tr>
          <td>{{unit.codigo}}</td><td>{{unit.numSerie}}</td><td>{{unit.estado}}</td><td>{{unit.observaciones}}</td>
          <td class="actions"><button (click)="edit()">Editar</button><button class="danger" (click)="remove()">Eliminar</button></td>
        </tr></tbody>
      </table>
    </section>
    <section class="panel">
      <h2>Disponibilidad por juego</h2>
      <button class="primary" (click)="loadAvailability()">Actualizar disponibilidad</button>
      <table *ngIf="availability.length">
        <thead><tr><th>Número de serie</th><th>Unidades disponibles</th></tr></thead>
        <tbody>
          @for(u of availability;track u.numSerie){
            <tr><td>{{u.numSerie}}</td><td>{{u.unidadesDisponibles}}</td></tr>
          }
        </tbody>
      </table>
    </section>
  </main>`,
  styles:[`
    .page{padding:28px;background:#0b1020;color:#fff;min-height:80vh}
    .panel{background:#1b2030;border:1px solid #465267;border-radius:14px;padding:22px;margin:20px 0}
    p{color:#aeb5c5}form{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:15px}
    label{display:grid;gap:8px;color:#c6ccd7}
    input,select{box-sizing:border-box;width:100%;padding:12px;background:#101827;color:white;border:1px solid #526078;border-radius:8px}
    button{padding:10px 14px;border-radius:8px;cursor:pointer;background:transparent;color:#22d3ee;border:1px solid #22d3ee}
    .primary{background:#d3b354;color:#171827;border-color:#d3b354}.danger{background:#a32154;color:white;border-color:#a32154}
    .actions,.search{display:flex;gap:9px;align-items:center;flex-wrap:wrap}.actions{grid-column:1/-1}
    table{width:100%;border-collapse:collapse;margin-top:16px}td,th{text-align:left;padding:12px;border-bottom:1px solid #465267}th{color:#22d3ee}
    .error{color:#ff7892}.message{color:#22d3ee}
    @media(max-width:650px){form{grid-template-columns:1fr}}
  `]
})
export class UnitsPageComponent implements OnInit {
  form:UnidadJuego={codigo:'',estado:'Disponible',observaciones:'',numSerie:''};
  unit:UnidadJuego|null=null;
  availability:UnidadDisponibilidad[]=[];
  searchCodigo='';
  searchSerie='';
  editing=false;
  saving=false;
  error='';
  message='';

  constructor(private service:UnidadJuegoService){}
  ngOnInit():void{this.loadAvailability();}

  loadAvailability():void{
    this.service.consultarDisponibilidad().subscribe({
      next:data=>this.availability=data,
      error:e=>this.error=e.error?.mensaje || 'Error al consultar disponibilidad.'
    });
  }

  loadOne():void{
    if(!this.searchCodigo.trim()||!this.searchSerie.trim()){
      this.error='Código y número de serie son obligatorios.';return;
    }
    this.error='';
    this.service.buscar(this.searchCodigo.trim(),this.searchSerie.trim()).subscribe({
      next:data=>this.unit=data,
      error:e=>{this.unit=null;this.error=e.error?.mensaje || 'No se encontró la unidad.';}
    });
  }

  save():void{
    if(!this.form.codigo.trim()||!this.form.numSerie.trim()||!this.form.estado){
      this.error='Completa código, serie y estado.';return;
    }
    this.saving=true;this.error='';
    const req=this.editing
      ? this.service.actualizar(this.form.codigo,this.form.numSerie,this.form)
      : this.service.crear(this.form);
    req.subscribe({
      next:data=>{
        this.saving=false;this.unit=data;
        this.searchCodigo=data.codigo;this.searchSerie=data.numSerie;
        this.message=this.editing?'Unidad actualizada.':'Unidad registrada.';
        this.cancel();this.loadAvailability();
      },
      error:e=>{this.saving=false;this.error=e.error?.mensaje || 'No se pudo guardar la unidad.';}
    });
  }

  edit():void{if(this.unit){this.form={...this.unit};this.editing=true;}}
  cancel():void{this.form={codigo:'',estado:'Disponible',observaciones:'',numSerie:''};this.editing=false;}

  remove():void{
    if(!this.unit||!confirm(`¿Eliminar la unidad ${this.unit.codigo}?`))return;
    this.service.eliminar(this.unit.codigo,this.unit.numSerie).subscribe({
      next:()=>{this.unit=null;this.message='Unidad eliminada.';this.loadAvailability();},
      error:e=>this.error=e.error?.mensaje || 'No se pudo eliminar.'
    });
  }
}
