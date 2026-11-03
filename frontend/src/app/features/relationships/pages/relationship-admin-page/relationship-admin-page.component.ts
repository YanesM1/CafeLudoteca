import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';

type Kind = 'beneficios' | 'frecuentes' | 'telefonos';

@Component({
  selector: 'app-relationship-admin-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <main class="page">
    <header>
      <div>
        <span>ADMINISTRACIÓN · GESTIÓN</span>
        <h1>{{title}}</h1>
        <p>Consulta, registra, actualiza y administra la información.</p>
      </div>
      <strong>{{rows.length}} registros</strong>
    </header>

    @if(kind === 'beneficios') {
      <section class="panel">
        <h2>{{editing ? 'Editar beneficio' : 'Registrar beneficio'}}</h2>
        <form (ngSubmit)="save()">
          <label>ID beneficio
            <input type="number" name="idBeneficio" [(ngModel)]="form.idBeneficio" required>
          </label>
          <label>ID membresía
            <input type="number" name="idMembresia" [(ngModel)]="form.idMembresia" required>
          </label>
          <label>Documento del socio
            <input name="documentoSocio" [(ngModel)]="form.documentoSocio" required>
          </label>
          <label>Descripción del beneficio
            <input name="beneficio" [(ngModel)]="form.beneficio" required>
          </label>
          <div class="actions">
            <button class="primary" [disabled]="saving">{{saving?'Guardando...':editing?'Actualizar':'Registrar'}}</button>
            <button type="button" (click)="reset()">Limpiar</button>
          </div>
        </form>
      </section>
      <section class="panel">
        <div class="heading">
          <h2>Beneficios registrados</h2>
          <button (click)="load()">Actualizar</button>
        </div>
        <div class="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Membresía</th><th>Socio</th><th>Beneficio</th><th>Acciones</th></tr></thead>
            <tbody>
              @for(r of rows; track r.idBeneficio + '-' + r.idMembresia + '-' + r.documentoSocio) {
                <tr>
                  <td>{{r.idBeneficio}}</td>
                  <td>{{r.idMembresia}}</td>
                  <td>{{r.documentoSocio}}</td>
                  <td>{{r.beneficio}}</td>
                  <td class="actions">
                    <button (click)="edit(r)">Editar</button>
                    <button class="danger" (click)="remove(r)">Eliminar</button>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      </section>
    }

    @if(kind === 'frecuentes') {
      <section class="panel">
        <h2>Buscar clientes frecuentes</h2>
        <div class="search">
          <input [(ngModel)]="searchValue" placeholder="Nivel de fidelidad">
          <button class="primary" (click)="search()">Buscar</button>
        </div>
      </section>
      <section class="panel">
        <h2>{{editing ? 'Actualizar cliente frecuente' : 'Registrar cliente frecuente'}}</h2>
        <form (ngSubmit)="save()">
          <label>Documento
            <input name="documentoCliente" [(ngModel)]="form.documentoCliente" required [disabled]="editing">
          </label>
          <label>Primer nombre
            <input name="primerNombre" [(ngModel)]="form.primerNombre" required>
          </label>
          <label>Segundo nombre
            <input name="segundoNombre" [(ngModel)]="form.segundoNombre">
          </label>
          <label>Primer apellido
            <input name="primerApellido" [(ngModel)]="form.primerApellido" required>
          </label>
          <label>Segundo apellido
            <input name="segundoApellido" [(ngModel)]="form.segundoApellido">
          </label>
          <label>Dirección
            <input name="direccion" [(ngModel)]="form.direccion">
          </label>
          <label>Fecha nacimiento
            <input type="date" name="fechaNacimiento" [(ngModel)]="form.fechaNacimiento">
          </label>
          <label>Puntos acumulados
            <input type="number" name="puntosAcumulados" [(ngModel)]="form.puntosAcumulados" min="0">
          </label>
          <label>Nivel de fidelidad
            <input name="nivelFidelidad" [(ngModel)]="form.nivelFidelidad" required>
          </label>
          <div class="actions">
            <button class="primary" [disabled]="saving">{{saving?'Guardando...':editing?'Actualizar':'Registrar'}}</button>
            <button type="button" (click)="reset()">Limpiar</button>
          </div>
        </form>
      </section>
      <section class="panel">
        <h2>Resultados</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Documento</th><th>Cliente</th><th>Puntos</th><th>Nivel</th><th>Acciones</th></tr></thead>
            <tbody>
              @for(r of rows; track r.documentoCliente || r.documento) {
                <tr>
                  <td>{{r.documentoCliente || r.documento}}</td>
                  <td>{{r.primerNombre}} {{r.primerApellido}}</td>
                  <td>{{r.puntosAcumulados}}</td>
                  <td>{{r.nivelFidelidad}}</td>
                  <td class="actions">
                    <button (click)="edit(r)">Editar</button>
                    <button class="danger" (click)="remove(r)">Eliminar</button>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      </section>
    }

    @if(kind === 'telefonos') {
      <section class="panel">
        <h2>Consultar teléfonos por cliente</h2>
        <div class="search">
          <input [(ngModel)]="searchValue" placeholder="Documento del cliente">
          <button class="primary" (click)="search()">Buscar</button>
        </div>
      </section>
      <section class="panel">
        <h2>{{editing ? 'Actualizar teléfono' : 'Registrar teléfono'}}</h2>
        <form (ngSubmit)="save()">
          <label>Documento cliente
            <input name="documento" [(ngModel)]="form.documento" required [disabled]="editing">
          </label>
          <label>Número de teléfono
            <input name="telefono" [(ngModel)]="form.telefono" required>
          </label>
          <div class="actions">
            <button class="primary" [disabled]="saving">{{saving?'Guardando...':editing?'Actualizar':'Registrar'}}</button>
            <button type="button" (click)="reset()">Limpiar</button>
          </div>
        </form>
      </section>
      <section class="panel">
        <h2>Teléfonos encontrados</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Documento</th><th>Teléfono</th><th>Acciones</th></tr></thead>
            <tbody>
              @for(r of rows; track r.documento + '-' + r.telefono) {
                <tr>
                  <td>{{r.documento}}</td>
                  <td>{{r.telefono}}</td>
                  <td class="actions">
                    <button (click)="edit(r)">Editar</button>
                    <button class="danger" (click)="remove(r)">Eliminar</button>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      </section>
    }

    @if(error) { <p class="error">{{error}}</p> }
    @if(message) { <p class="message">{{message}}</p> }
    @if(loading) { <p>Consultando información...</p> }
  </main>
  `,
  styles: [`
    .page{padding:28px;background:#0b1020;color:#fff;min-height:80vh}
    header,.heading,.search{display:flex;justify-content:space-between;align-items:center;gap:15px}
    header span{color:#22d3ee;font-size:12px;font-weight:800;letter-spacing:2px}
    h1{font-size:30px;margin:9px 0}
    p{color:#aeb5c5}
    .panel{background:#1b2030;border:1px solid #465267;border-radius:14px;padding:22px;margin:20px 0}
    form{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:15px}
    label{display:grid;gap:8px;color:#c6ccd7}
    input{box-sizing:border-box;width:100%;padding:12px;background:#101827;color:white;border:1px solid #526078;border-radius:8px}
    button{padding:10px 14px;border-radius:8px;cursor:pointer;background:transparent;color:#22d3ee;border:1px solid #22d3ee}
    button:disabled{opacity:.5;cursor:wait}
    .primary{background:#d3b354;color:#171827;border-color:#d3b354}
    .danger{background:#a32154;color:white;border-color:#a32154}
    .actions{display:flex;gap:8px;align-items:center;flex-wrap:wrap}
    table{width:100%;border-collapse:collapse}
    td,th{text-align:left;padding:12px;border-bottom:1px solid #465267}
    th{color:#22d3ee}
    .table-wrap{overflow-x:auto}
    .error{color:#ff7892}.message{color:#22d3ee}
    @media(max-width:650px){form{grid-template-columns:1fr}.search{align-items:stretch;flex-direction:column}}
  `]
})
export class RelationshipAdminPageComponent implements OnInit {
  kind: Kind;
  rows:any[]=[];
  form:any={};
  searchValue='';
  originalPhone='';
  editing=false;
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

  ngOnInit():void {
    this.reset();
    if(this.kind==='beneficios')this.load();
  }

  reset():void {
    this.editing=false;
    this.originalPhone='';
    if(this.kind==='beneficios')
      this.form={idBeneficio:null,idMembresia:null,documentoSocio:'',beneficio:''};
    else if(this.kind==='frecuentes')
      this.form={documentoCliente:'',primerNombre:'',segundoNombre:'',primerApellido:'',segundoApellido:'',direccion:'',fechaNacimiento:'',puntosAcumulados:0,nivelFidelidad:''};
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
      if(!this.searchValue.trim()){this.loading=false;this.error='Escribe el nivel de fidelidad.';return;}
      const params=new HttpParams().set('nivelFidelidad',this.searchValue.trim());
      this.http.get<any[]>('/api/clienteFrecuente/buscar',{params}).subscribe({
        next:r=>{this.rows=r;this.loading=false;},
        error:e=>this.fail(e,'No fue posible consultar clientes frecuentes.')
      });
    } else if(this.kind==='telefonos') {
      if(!this.searchValue.trim()){this.loading=false;this.error='Escribe el documento del cliente.';return;}
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
      request={...this.form};
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
        const filtro = this.kind==='frecuentes'
          ? this.form.nivelFidelidad
          : this.form.documento;
        if(filtro) this.searchValue=filtro;
        this.reset();
        if(this.kind==='beneficios')this.load();
        else this.search();
      },
      error:e=>this.fail(e,'No fue posible guardar el registro.')
    });
  }

  edit(r:any):void {
    this.error='';this.message='';this.editing=true;
    this.form={...r};
    if(this.kind==='frecuentes') {
      this.form.documentoCliente=r.documentoCliente||r.documento;
      this.form.fechaNacimiento=this.dateInput(r.fechaNacimiento);
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

  private dateInput(value:any):string {
    if(!value)return '';
    if(Array.isArray(value))
      return `${value[0]}-${String(value[1]).padStart(2,'0')}-${String(value[2]).padStart(2,'0')}`;
    return String(value).substring(0,10);
  }

  private fail(e:any,fallback:string):void {
    this.loading=false;this.saving=false;
    this.error=typeof e.error==='string'?e.error:e.error?.mensaje||fallback;
  }
}
