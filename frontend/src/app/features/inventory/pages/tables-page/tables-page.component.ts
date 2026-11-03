import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Mesa, MesaService } from '../../../../core/services/mesa.service';

@Component({
 selector:'app-tables-page',
 standalone:true,
 imports:[CommonModule,FormsModule],
 template:`
 <main class="page">
  <header><div><span>ADMINISTRACIÓN · OPERACIÓN</span><h1>Mesas</h1><p>Gestiona las mesas de Café & Ludoteca.</p></div><strong>{{mesas.length}} mesas</strong></header>
  <section class="panel">
   <h2>{{editing?'Editar mesa':'Registrar mesa'}}</h2>
   <form (ngSubmit)="save()">
    <label>ID de mesa<input name="id" [(ngModel)]="form.idMesa" [disabled]="editing" required></label>
    <label>Capacidad<input type="number" name="capacidad" [(ngModel)]="form.capacidad" min="1" required></label>
    <label>Temática<input name="tematica" [(ngModel)]="form.tematica" required></label>
    <label>Ubicación<input name="ubicacion" [(ngModel)]="form.ubicacion" required></label>
    <div class="actions"><button class="primary" [disabled]="saving">{{saving?'Guardando...':editing?'Actualizar':'Registrar mesa'}}</button><button type="button" *ngIf="editing" (click)="cancel()">Cancelar</button></div>
   </form>
  </section>
  <section class="panel">
   <div class="search"><h2>Mesas registradas</h2><button class="primary" (click)="load()">Actualizar</button></div>
   <p class="error">{{error}}</p><p class="message">{{message}}</p>
   <p *ngIf="loading">Consultando mesas...</p>
   <p *ngIf="!loading&&!mesas.length">No hay mesas registradas.</p>
   <div class="table-wrap" *ngIf="mesas.length">
    <table><thead><tr><th>ID</th><th>Capacidad</th><th>Temática</th><th>Ubicación</th><th>Acciones</th></tr></thead>
     <tbody>@for(m of mesas;track m.idMesa){<tr><td>{{m.idMesa}}</td><td>{{m.capacidad}}</td><td>{{m.tematica}}</td><td>{{m.ubicacion}}</td><td class="actions"><button (click)="edit(m)">Editar</button><button class="danger" (click)="remove(m)">Eliminar</button></td></tr>}</tbody>
    </table>
   </div>
  </section>
 </main>`,
 styles:[`
 .page{padding:28px;background:#0b1020;color:#fff;min-height:80vh}
 header,.search{display:flex;justify-content:space-between;align-items:center;gap:15px}
 header span{color:#22d3ee;font-weight:800;letter-spacing:2px;font-size:12px}
 h1{font-size:30px;margin:9px 0}p{color:#aeb5c5}
 .panel{background:#1b2030;border:1px solid #465267;border-radius:14px;padding:22px;margin:20px 0}
 form{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:15px}
 label{display:grid;gap:8px;color:#c6ccd7}
 input{box-sizing:border-box;width:100%;padding:12px;background:#101827;color:white;border:1px solid #526078;border-radius:8px}
 button{padding:10px 14px;border-radius:8px;cursor:pointer;background:transparent;color:#22d3ee;border:1px solid #22d3ee}
 .primary{background:#d3b354;color:#171827;border-color:#d3b354}.danger{background:#a32154;color:white;border-color:#a32154}
 .actions{display:flex;gap:9px;align-items:center;flex-wrap:wrap}.actions:first-child{grid-column:1/-1}
 table{width:100%;border-collapse:collapse}td,th{text-align:left;padding:12px;border-bottom:1px solid #465267}th{color:#22d3ee}
 .table-wrap{overflow-x:auto}.error{color:#ff7892}.message{color:#22d3ee}
 @media(max-width:650px){form{grid-template-columns:1fr}}
 `]
})
export class TablesPageComponent implements OnInit {
 mesas:Mesa[]=[];
 form:Mesa={idMesa:'',capacidad:1,tematica:'',ubicacion:''};
 editing=false;saving=false;loading=false;error='';message='';
 constructor(private service:MesaService){}
 ngOnInit():void{this.load();}
 load():void{
  this.loading=true;this.error='';
  this.service.listar().subscribe({
   next:data=>{this.mesas=data;this.loading=false;},
   error:e=>{this.error=e.error?.mensaje||'No fue posible consultar mesas.';this.loading=false;}
  });
 }
 save():void{
  if(!this.form.idMesa.trim()||!this.form.tematica.trim()||!this.form.ubicacion.trim()||this.form.capacidad<1){
   this.error='Completa los datos de la mesa.';return;
  }
  this.saving=true;this.error='';
  const data={...this.form};
  const req=this.editing?this.service.actualizar(data.idMesa,data):this.service.crear(data);
  req.subscribe({
   next:()=>{this.saving=false;this.message=this.editing?'Mesa actualizada.':'Mesa registrada.';this.cancel();this.load();},
   error:e=>{this.saving=false;this.error=e.error?.mensaje||'No fue posible guardar la mesa.';}
  });
 }
 edit(m:Mesa):void{this.form={...m};this.editing=true;}
 cancel():void{this.form={idMesa:'',capacidad:1,tematica:'',ubicacion:''};this.editing=false;}
 remove(m:Mesa):void{
  if(!confirm(`¿Eliminar mesa ${m.idMesa}?`))return;
  this.service.eliminar(m.idMesa).subscribe({
   next:()=>{this.message='Mesa eliminada.';this.load();},
   error:e=>this.error=e.error?.mensaje||'No fue posible eliminar la mesa.'
  });
 }
}
