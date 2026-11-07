import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';

type Kind = 'beneficios' | 'frecuentes' | 'telefonos';

import { ClienteResponse } from '../../../../core/models/cliente.model';
import { mensajeError } from '../../../../core/utils/http-error';
@Component({
  selector: 'app-relationship-admin-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <section class="cl-page">
    <header class="cl-page-header">
      <div>
        <span class="cl-eyebrow">{{ eyebrow }}</span>
        <h1 class="cl-title">{{ title }}</h1>
        <p class="cl-subtitle">{{ subtitle }}</p>
      </div>
      <div class="cl-page-header__aside">
        <span class="cl-count"><strong>{{ visibles.length }}</strong>registros</span>
        <button class="btn btn--gold" type="button" (click)="nuevo()">+ Nuevo {{ nuevoLabel.toLowerCase() }}</button>
      </div>
    </header>

    @if (message) { <p class="cl-alert cl-alert--success">{{ message }}</p> }
    @if (error && !formAbierto) { <p class="cl-alert cl-alert--error">{{ error }}</p> }

    <section class="cl-card">
      <form class="cl-toolbar" (ngSubmit)="buscar()">
        <input class="form-control" name="busqueda" [(ngModel)]="searchValue" [placeholder]="placeholder">
        <button class="btn btn--gold" type="submit">Buscar</button>
        <button class="btn btn--ghost" type="button" (click)="searchValue='';buscar()">Ver todos</button>
      </form>

      @if (loading) {
        <p class="cl-empty">Consultando información...</p>
      } @else if (!visibles.length) {
        <p class="cl-empty">No hay registros para mostrar.</p>
      } @else {
        <div class="cl-table-wrap">
          <table class="cl-table">
            @if (kind === 'beneficios') {
              <thead><tr><th>ID</th><th>Beneficio</th><th>Membresía</th><th>Socio</th><th class="cl-table__actions-head">Acciones</th></tr></thead>
              <tbody>
                @for (r of visibles; track r.idBeneficio + '-' + r.idMembresia + '-' + r.documentoSocio) {
                  <tr>
                    <td>{{ r.idBeneficio }}</td><td>{{ r.beneficio }}</td><td>#{{ r.idMembresia }}</td>
                    <td>{{ nombres[r.documentoSocio] || r.documentoSocio }} <span class="cl-muted">· {{ r.documentoSocio }}</span></td>
                    <td><div class="cl-table__actions">
                      <button class="btn btn--ghost btn--sm" type="button" (click)="edit(r)">Editar</button>
                      <button class="btn btn--danger btn--sm" type="button" (click)="remove(r)">Eliminar</button>
                    </div></td>
                  </tr>
                }
              </tbody>
            }
            @if (kind === 'frecuentes') {
              <thead><tr><th>Documento</th><th>Cliente</th><th>Puntos</th><th>Nivel</th><th class="cl-table__actions-head">Acciones</th></tr></thead>
              <tbody>
                @for (r of visibles; track r.documentoCliente || r.documento) {
                  <tr>
                    <td>{{ r.documentoCliente || r.documento }}</td>
                    <td>{{ nombres[r.documentoCliente || r.documento] || '—' }}</td>
                    <td>{{ r.puntosAcumulados }}</td><td>{{ r.nivelFidelidad }}</td>
                    <td><div class="cl-table__actions">
                      <button class="btn btn--ghost btn--sm" type="button" (click)="edit(r)">Editar</button>
                      <button class="btn btn--danger btn--sm" type="button" (click)="remove(r)">Eliminar</button>
                    </div></td>
                  </tr>
                }
              </tbody>
            }
            @if (kind === 'telefonos') {
              <thead><tr><th>Documento</th><th>Cliente</th><th>Teléfono</th><th class="cl-table__actions-head">Acciones</th></tr></thead>
              <tbody>
                @for (r of visibles; track r.documento + '-' + r.telefono) {
                  <tr>
                    <td>{{ r.documento }}</td><td>{{ nombres[r.documento] || '—' }}</td><td>{{ r.telefono }}</td>
                    <td><div class="cl-table__actions">
                      <button class="btn btn--ghost btn--sm" type="button" (click)="edit(r)">Editar</button>
                      <button class="btn btn--danger btn--sm" type="button" (click)="remove(r)">Eliminar</button>
                    </div></td>
                  </tr>
                }
              </tbody>
            }
          </table>
        </div>
      }
    </section>

    @if (formAbierto) {
      <div class="cl-modal-overlay" (click)="reset()">
        <section class="cl-card cl-modal" role="dialog" aria-modal="true" (click)="$event.stopPropagation()">
          <h2 class="cl-card__title">{{ editing ? 'Editar' : 'Nuevo' }} {{ nuevoLabel.toLowerCase() }}</h2>
          @if (error) { <p class="cl-alert cl-alert--error">{{ error }}</p> }
          <form class="cl-form-grid" (ngSubmit)="save()">
            @if (kind === 'beneficios') {
              <label class="cl-field cl-field--full">Membresía *
                <select class="form-control" name="membresia" [(ngModel)]="form.membresiaClave" required [disabled]="editing">
                  <option value="" disabled>Selecciona una membresía</option>
                  @for (m of opcionesMembresia; track m.value) {
                    <option [value]="m.value">{{ m.label }}</option>
                  }
                </select>
              </label>
              <label class="cl-field cl-field--full">Beneficio *
                <input class="form-control" name="beneficio" maxlength="100" [(ngModel)]="form.beneficio" required
                  list="beneficios-sugeridos" placeholder="Elige una sugerencia o escribe otro">
                <datalist id="beneficios-sugeridos">
                  @for (b of beneficiosSugeridos; track b) { <option [value]="b"></option> }
                </datalist>
              </label>
            }
            @if (kind === 'frecuentes') {
              <label class="cl-field cl-field--full">Cliente *
                <select class="form-control" name="documentoCliente" [(ngModel)]="form.documentoCliente" required [disabled]="editing">
                  <option value="" disabled>Selecciona un cliente</option>
                  @for (c of opcionesCliente(form.documentoCliente); track c.value) {
                    <option [value]="c.value">{{ c.label }}</option>
                  }
                </select>
              </label>
              <label class="cl-field">Puntos acumulados
                <input class="form-control" type="number" name="puntosAcumulados" [(ngModel)]="form.puntosAcumulados" min="0">
              </label>
              <label class="cl-field">Nivel de fidelidad *
                <select class="form-control" name="nivelFidelidad" [(ngModel)]="form.nivelFidelidad" required>
                  @for (n of nivelesFidelidad; track n) { <option [value]="n">{{ n }}</option> }
                </select>
              </label>
            }
            @if (kind === 'telefonos') {
              <label class="cl-field cl-field--full">Cliente *
                <select class="form-control" name="documento" [(ngModel)]="form.documento" required [disabled]="editing">
                  <option value="" disabled>Selecciona un cliente</option>
                  @for (c of opcionesCliente(form.documento); track c.value) {
                    <option [value]="c.value">{{ c.label }}</option>
                  }
                </select>
              </label>
              <label class="cl-field cl-field--full">Número de teléfono *
                <input class="form-control" name="telefono" [(ngModel)]="form.telefono" required
                  inputmode="numeric" maxlength="15" placeholder="Solo números, entre 7 y 15 dígitos">
              </label>
            }
            <div class="cl-form-actions">
              <button class="btn btn--ghost" type="button" (click)="reset()">Cancelar</button>
              <button class="btn btn--gold" type="submit" [disabled]="saving">{{ saving ? 'Guardando...' : 'Guardar ' + nuevoLabel.toLowerCase() }}</button>
            </div>
          </form>
        </section>
      </div>
    }
  </section>

  `,
  styles: [':host { display: block; }']
})
export class RelationshipAdminPageComponent implements OnInit {
  kind: Kind;
  rows:any[]=[];
  form:any={};
  searchValue='';
  originalPhone='';
  editing=false;
  formAbierto=false;
  loading=false;
  saving=false;
  error='';
  message='';

  constructor(private route:ActivatedRoute,private http:HttpClient) {
    this.kind=this.route.snapshot.data['kind'] as Kind;
  }

  get title():string {
    return this.kind==='beneficios'?'Beneficios':
      this.kind==='frecuentes'?'Clientes frecuentes':'Teléfonos';
  }

  /** Documento -> nombre del cliente (para mostrar a quién pertenece cada registro). */
  nombres:Record<string,string>={};

  ngOnInit():void {
    this.reset();
    this.buscar();
    // Nombres de clientes (para mostrar a quién pertenece cada registro) y listas del formulario
    this.http.get<ClienteResponse[]>('/api/cliente/buscar').subscribe({
      next:lista=>{
        lista.forEach(c=>this.nombres[c.documentoCliente]=
          [c.primerNombre,c.primerApellido].filter(Boolean).join(' '));
        this.clientes=lista
          .map(c=>({value:c.documentoCliente,label:`${this.nombres[c.documentoCliente]} · ${c.documentoCliente}`}))
          .sort((a,b)=>a.label.localeCompare(b.label));
      },
      error:()=>{}
    });
    if(this.kind==='beneficios') this.cargarMembresias();
  }

  /** Clientes para los desplegables (incluye el actual al editar). */
  clientes:{value:string;label:string}[]=[];
  opcionesCliente(actual:string){
    return actual && !this.clientes.some(c=>c.value===actual)
      ? [{value:actual,label:actual},...this.clientes] : this.clientes;
  }

  /** Niveles de fidelidad para clientes frecuentes. */
  get nivelesFidelidad():string[]{
    const base=['Bronce','Plata','Oro'];
    const actual=this.form?.nivelFidelidad;
    return actual && !base.includes(actual) ? [...base,actual] : base;
  }

  /** Beneficios que menciona la propuesta para los socios. */
  readonly beneficiosSugeridos=[
    'Descuento en consumo',
    'Mayor cantidad de préstamos simultáneos',
    'Prioridad en reservas'
  ];

  opcionesMembresia:{value:string;label:string}[]=[];
  private cargarMembresias():void{
    this.http.get<any[]>('/api/membresia/buscar').subscribe({
      next:lista=>this.opcionesMembresia=lista.map(m=>({
        value:`${m.idMembresia}::${m.documento}`,
        label:`#${m.idMembresia} · ${m.plan} · ${this.nombres[m.documento]||m.documento} (${m.estado})`
      })),
      error:()=>{}
    });
  }

  /** Beneficios se filtran en pantalla; frecuentes y teléfonos consultan al backend. */
  get visibles():any[] {
    if(this.kind!=='beneficios'||!this.filtroAplicado) return this.rows;
    const t=this.filtroAplicado.toLowerCase();
    return this.rows.filter(r=>
      [r.beneficio,r.documentoSocio,this.nombres[r.documentoSocio],String(r.idMembresia)]
        .some(v=>(v||'').toString().toLowerCase().includes(t)));
  }

  filtroAplicado='';

  get placeholder():string {
    return this.kind==='beneficios' ? 'Buscar por beneficio, socio o membresía'
      : this.kind==='frecuentes' ? 'Nivel de fidelidad (vacío = todos)'
      : 'Documento del cliente (vacío = todos)';
  }

  buscar():void {
    if(this.kind==='beneficios'){ this.filtroAplicado=this.searchValue.trim(); this.load(); }
    else this.search();
  }

  get eyebrow():string {
    return this.kind==='beneficios'?'Administración · Membresías':'Administración · Clientes';
  }

  get subtitle():string {
    return this.kind==='beneficios'
      ? 'Beneficios incluidos en cada membresía (descuentos, prioridad en reservas...).'
      : this.kind==='frecuentes'
        ? 'Clientes registrados que acumulan puntos por fidelidad.'
        : 'Números de contacto de cada cliente.';
  }

  get nuevoLabel():string {
    return this.kind==='beneficios'?'Beneficio':this.kind==='frecuentes'?'Cliente frecuente':'Teléfono';
  }

  nuevo():void {
    this.reset();
    this.error='';this.message='';
    this.formAbierto=true;
  }

  reset():void {
    this.editing=false;
    this.formAbierto=false;
    this.originalPhone='';
    if(this.kind==='beneficios')
      this.form={idBeneficio:null,membresiaClave:'',beneficio:''};
    else if(this.kind==='frecuentes')
      // El cliente ya debe existir: aquí solo se registran puntos y nivel.
      this.form={documentoCliente:'',puntosAcumulados:0,nivelFidelidad:'Bronce'};
    else
      this.form={documento:'',telefono:''};
  }

  load():void {
    this.loading=true;this.error='';
    this.http.get<any[]>('/api/beneficio').subscribe({
      next:r=>{this.rows=r;this.loading=false;},
      error:e=>{this.fail(e,'No fue posible cargar los beneficios.');}
    });
  }

  search():void {
    this.loading=true;this.error='';this.message='';
    if(this.kind==='frecuentes') {
      const nivel=this.searchValue.trim();
      const peticion=nivel
        ? this.http.get<any[]>('/api/clienteFrecuente/buscar',{params:new HttpParams().set('nivelFidelidad',nivel)})
        : this.http.get<any[]>('/api/clienteFrecuente');
      peticion.subscribe({
        next:r=>{this.rows=r;this.loading=false;},
        error:e=>this.fail(e,'No fue posible consultar clientes frecuentes.')
      });
    } else if(this.kind==='telefonos') {
      const params=new HttpParams().set('documento',this.searchValue.trim());
      this.http.get<any[]>('/api/telefono/buscar',{params}).subscribe({
        next:r=>{this.rows=r;this.loading=false;},
        error:e=>this.fail(e,'No fue posible consultar teléfonos.')
      });
    }
  }

  save():void {
    this.error='';this.message='';
    let url='';
    let request:any;
    let method:any;

    if(this.kind==='beneficios') {
      const [idMembresia,documentoSocio]=String(this.form.membresiaClave||'').split('::');
      if(!idMembresia||!documentoSocio){this.error='Selecciona la membresía.';return;}
      // Al crear, el backend asigna el número de beneficio
      request={idBeneficio:this.form.idBeneficio||0,idMembresia:Number(idMembresia),
        documentoSocio,beneficio:this.form.beneficio};
      url='/api/beneficio';
      method=this.editing?'put':'post';
      if(this.editing)
        url+=`/${request.idBeneficio}/${request.idMembresia}/${encodeURIComponent(request.documentoSocio)}`;
    } else if(this.kind==='frecuentes') {
      request={...this.form};
      url='/api/clienteFrecuente';
      method=this.editing?'put':'post';
      if(this.editing)url+=`/${encodeURIComponent(request.documentoCliente)}`;
    } else {
      request={...this.form};
      url='/api/telefono';
      method=this.editing?'put':'post';
      if(this.editing)
        url+=`/${encodeURIComponent(request.documento)}/${encodeURIComponent(this.originalPhone)}`;
    }

    if(this.saving)return;
    this.saving=true;
    const call=method==='put'?this.http.put(url,request):this.http.post(url,request);
    call.subscribe({
      next:()=>{
        this.saving=false;
        this.message=this.editing?'Registro actualizado.':'Registro creado.';
        this.reset();
        this.buscar();
      },
      error:e=>this.fail(e,'No fue posible guardar el registro.')
    });
  }

  edit(r:any):void {
    this.error='';this.message='';this.editing=true;this.formAbierto=true;
    this.form={...r};
    if(this.kind==='beneficios') this.form.membresiaClave=`${r.idMembresia}::${r.documentoSocio}`;
    if(this.kind==='frecuentes') {
      this.form={documentoCliente:r.documentoCliente||r.documento,
        puntosAcumulados:r.puntosAcumulados,nivelFidelidad:r.nivelFidelidad};
    }
    if(this.kind==='telefonos')this.originalPhone=r.telefono;
  }

  remove(r:any):void {
    if(!confirm('¿Confirmas que deseas eliminar este registro?'))return;
    let url='';
    if(this.kind==='beneficios')
      url=`/api/beneficio/${r.idBeneficio}/${r.idMembresia}/${encodeURIComponent(r.documentoSocio)}`;
    else if(this.kind==='frecuentes')
      url=`/api/clienteFrecuente/${encodeURIComponent(r.documentoCliente||r.documento)}`;
    else
      url=`/api/telefono/${encodeURIComponent(r.documento)}/${encodeURIComponent(r.telefono)}`;

    this.http.delete(url).subscribe({
      next:()=>{
        this.message='Registro eliminado.';
        if(this.kind==='beneficios')this.load();
        else this.search();
      },
      error:e=>this.fail(e,'No fue posible eliminar el registro.')
    });
  }


  private fail(e:any,fallback:string):void {
    this.loading=false;this.saving=false;
    this.error=mensajeError(e,fallback);
  }
}
