import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ElementoJuego, ElementoJuegoService } from '../../../../core/services/elemento-juego.service';

@Component({
  selector:'app-elements-page',
  standalone:true,
  imports:[CommonModule,FormsModule],
  template:`
  <main class="page">
    <h1>Elementos de juego</h1>
    <p>Registra y administra los componentes que pertenecen a cada juego.</p>
    <section class="panel">
      <h2>{{editing?'Editar elemento':'Registrar elemento'}}</h2>
      <form (ngSubmit)="save()">
        <label>Descripción del elemento
          <input name="elemento" [(ngModel)]="form.elementoJuego" required>
        </label>
        <label>Número de serie del juego
          <input name="serie" [(ngModel)]="form.numSerie" required>
        </label>
        <label *ngIf="editing">ID del elemento
          <input [value]="form.idElementoJuego" disabled>
        </label>
        <div class="actions">
          <button class="primary" [disabled]="saving">{{saving?'Guardando...':editing?'Actualizar':'Registrar'}}</button>
          <button type="button" *ngIf="editing" (click)="cancel()">Cancelar</button>
        </div>
      </form>
    </section>
    <section class="panel">
      <h2>Consultar elemento por ID</h2>
      <div class="search">
        <input type="number" [(ngModel)]="searchId" placeholder="ID del elemento">
        <button class="primary" (click)="load()">Consultar</button>
      </div>
      <p class="error">{{error}}</p>
      <p class="message">{{message}}</p>
      <table *ngIf="item">
        <thead><tr><th>ID</th><th>Elemento</th><th>Juego</th><th>Acciones</th></tr></thead>
        <tbody><tr>
          <td>{{item.idElementoJuego}}</td><td>{{item.elementoJuego}}</td><td>{{item.numSerie}}</td>
          <td class="actions">
            <button (click)="edit()">Editar</button>
            <button class="danger" (click)="remove()">Eliminar</button>
          </td>
        </tr></tbody>
      </table>
      <p *ngIf="searched && !item">No se encontró el elemento.</p>
    </section>
  </main>`,
  styles:[`
    .page{padding:28px;background:#0b1020;color:#fff;min-height:80vh}
    .panel{background:#1b2030;border:1px solid #465267;border-radius:14px;padding:22px;margin:20px 0}
    p{color:#aeb5c5}form{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:15px}
    label{display:grid;gap:8px;color:#c6ccd7}
    input{box-sizing:border-box;width:100%;padding:12px;background:#101827;color:white;border:1px solid #526078;border-radius:8px}
    button{padding:10px 14px;border-radius:8px;cursor:pointer;background:transparent;color:#22d3ee;border:1px solid #22d3ee}
    .primary{background:#d3b354;color:#171827;border-color:#d3b354}.danger{background:#a32154;color:white;border-color:#a32154}
    .actions,.search{display:flex;gap:9px;align-items:center;flex-wrap:wrap}.actions{grid-column:1/-1}
    table{width:100%;border-collapse:collapse}td,th{text-align:left;padding:12px;border-bottom:1px solid #465267}th{color:#22d3ee}
    .error{color:#ff7892}.message{color:#22d3ee}
    @media(max-width:650px){form{grid-template-columns:1fr}}
  `]
})
export class ElementsPageComponent {
  form:ElementoJuego={elementoJuego:'',numSerie:''};
  item:ElementoJuego|null=null;
  searchId:number|null=null;
  editing=false;
  saving=false;
  searched=false;
  error='';
  message='';

  constructor(private service:ElementoJuegoService){}

  load():void{
    if(!this.searchId || this.searchId<=0){this.error='Ingresa un ID válido.';return;}
    this.error='';this.searched=true;this.item=null;
    this.service.buscarId(this.searchId).subscribe({
      next:data=>this.item=data,
      error:e=>this.error=e.error?.mensaje || 'No se encontró el elemento.'
    });
  }

  save():void{
    if(!this.form.elementoJuego.trim()||!this.form.numSerie.trim()){
      this.error='Completa el elemento y el número de serie.';return;
    }
    this.saving=true;this.error='';
    const req=this.editing
      ? this.service.actualizar(this.form.idElementoJuego!,this.form)
      : this.service.crear(this.form);
    req.subscribe({
      next:data=>{
        this.saving=false;this.item=data;
        this.searchId=data.idElementoJuego || null;
        this.message=this.editing?'Elemento actualizado.':'Elemento registrado.';
        this.cancel(false);
      },
      error:e=>{this.saving=false;this.error=e.error?.mensaje || 'No se pudo guardar el elemento.';}
    });
  }

  edit():void{
    if(!this.item)return;
    this.form={...this.item};this.editing=true;
  }

  cancel(reset=true):void{
    this.form={elementoJuego:'',numSerie:''};this.editing=false;
    if(reset)this.item=null;
  }

  remove():void{
    if(!this.item?.idElementoJuego||!confirm('¿Eliminar este elemento?'))return;
    this.service.eliminar(this.item.idElementoJuego).subscribe({
      next:()=>{this.item=null;this.message='Elemento eliminado.';},
      error:e=>this.error=e.error?.mensaje || 'No se pudo eliminar.'
    });
  }
}
