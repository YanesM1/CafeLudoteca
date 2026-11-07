import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import {
  LoanFormComponent, LoanFormData, LoanAuditData
} from '../../../../shared/organisms/loan-form/loan-form.component';
import { SelectOption } from '../../../../shared/atoms/select/select.component';
import {
  NotificationToastComponent, NotificationType
} from '../../../../shared/molecules/notification-toast/notification-toast.component';
import { PrestamoResponse, PrestamoService, UnidadPrestada } from '../../../../core/services/prestamo.service';
import { Membresia } from '../../../../core/services/membresia.service';
import { Socio } from '../../../../core/services/socio.service';
import { JuegoMesaResponse } from '../../../../core/models/juego-mesa.model';
import { ClienteResponse } from '../../../../core/models/cliente.model';
import { Empleado } from '../../../../core/models/empleado.model';
import { UnidadJuego } from '../../../../core/services/unidad-juego.service';
import { mensajeError } from '../../../../core/utils/http-error';
import { fechaCorta, fechaISO } from '../../../../core/utils/fecha';

@Component({
  selector:'app-loans-page',
  standalone:true,
  imports:[CommonModule,LoanFormComponent,NotificationToastComponent],
  templateUrl:'./loans-page.component.html',
  styleUrl:'./loans-page.component.css'
})
export class LoansPageComponent implements OnInit {
  @ViewChild(LoanFormComponent) private loanForm?: LoanFormComponent;

  clienteOptions:SelectOption[]=[];
  /** Situación del cliente elegido (socio, membresía) para avisar antes de registrar. */
  clienteInfo='';
  clienteAviso=false;
  activeLoanDetail='';
  private socios=new Set<string>();
  private membresias:Membresia[]=[];
  private nombresJuego:Record<string,string>={};
  private activos:PrestamoResponse[]=[];
  unitOptions:SelectOption[]=[];
  employeeOptions:SelectOption[]=[];
  componentStatusOptions:SelectOption[]=[
    {value:'COMPLETO',label:'Completo'},
    {value:'INCOMPLETO',label:'Incompleto (faltan piezas)'},
    {value:'DANADO',label:'Dañado (a mantenimiento)'}
  ];
  activeLoanOptions:SelectOption[]=[];
  activeLoanId='';
  submitting=false;
  loading=false;
  errorMessage='';
  notificationVisible=false;
  notificationType:NotificationType='info';
  notificationTitle='';
  notificationMessage='';

  historial:PrestamoResponse[]=[];
  cargandoHistorial=false;

  constructor(
    private http:HttpClient,
    private loans:PrestamoService
  ){}

  ngOnInit():void{
    this.loadData();
  }

  loadData():void{
    this.loading=true;
    this.errorMessage='';
    forkJoin({
      clients:this.http.get<ClienteResponse[]>('/api/cliente/buscar'),
      employees:this.http.get<Empleado[]>('/api/empleado/listar'),
      units:this.http.get<UnidadJuego[]>('/api/unidadJuego/disponibles'),
      active:this.loans.activos(),
      socios:this.http.get<Socio[]>('/api/socio').pipe(catchError(()=>of([] as Socio[]))),
      membresias:this.http.get<Membresia[]>('/api/membresia/buscar').pipe(catchError(()=>of([] as Membresia[]))),
      juegos:this.http.get<JuegoMesaResponse[]>('/api/juegoMesa/buscar').pipe(catchError(()=>of([] as JuegoMesaResponse[])))
    }).subscribe({
      next:r=>{
        this.socios=new Set(r.socios.map(s=>s.documento));
        this.membresias=r.membresias;
        r.juegos.forEach(j=>this.nombresJuego[j.numSerie]=j.nombre);
        this.activos=r.active;
        this.clienteOptions=r.clients.map(c=>({
          value:c.documentoCliente,
          label:`${c.primerNombre||''} ${c.primerApellido||''} · ${c.documentoCliente}`
        }));
        this.employeeOptions=r.employees.map(e=>({
          value:e.documentoEmp,
          label:`${e.nombre} ${e.apellido} · ${e.documentoEmp}`
        }));
        this.unitOptions=r.units.map(u=>({
          value:u.codigo+'::'+u.numSerie,
          label:`${this.nombresJuego[u.numSerie]||u.numSerie} · ${u.codigo}`
        })).sort((a,b)=>a.label.localeCompare(b.label));
        const nombreCliente=(doc:string)=>this.clienteOptions.find(c=>c.value===doc)?.label.split(' · ')[0]||doc;
        this.activeLoanOptions=r.active.map(p=>({
          value:String(p.idPrestamo),
          label:`#${p.idPrestamo} · ${nombreCliente(p.documentoCliente)} · ${this.unidadesDe(p).length} juego(s)`
        }));
        this.loading=false;
      },
      error:e=>{
        this.loading=false;
        this.errorMessage=mensajeError(e,'Error cargando los datos operativos.');
      }
    });
    this.cargarHistorial();
  }

  /** Unidades de un préstamo (lista nueva o, en préstamos antiguos, la unidad única). */
  unidadesDe(p:PrestamoResponse):UnidadPrestada[]{
    if(p.unidades?.length) return p.unidades;
    return p.codigoUnidad?[{codigo:p.codigoUnidad,numSerie:p.numSerieUnidad||''}]:[];
  }

  /** Revisa si el cliente puede pedir el tipo de préstamo elegido (RN-02, RN-07). */
  onClienteChange(e:{documento:string;tipo:string}):void{
    if(!e.documento){this.clienteInfo='';this.clienteAviso=false;return;}
    const esSocio=this.socios.has(e.documento);
    const hoy=new Date().toISOString().substring(0,10);
    const activa=this.membresias.find(m=>m.documento===e.documento&&m.estado==='Activa'
      &&(!m.fechaFin||fechaISO(m.fechaFin)>=hoy));
    if(!esSocio){
      this.clienteAviso=e.tipo==='externo';
      this.clienteInfo=this.clienteAviso
        ?'Este cliente no es socio: solo puede pedir préstamos internos.'
        :'Cliente sin membresía: puede pedir préstamos internos.';
    } else if(!activa){
      this.clienteAviso=true;
      this.clienteInfo='Es socio, pero no tiene una membresía activa: no puede pedir préstamos.';
    } else {
      this.clienteAviso=false;
      this.clienteInfo=`Socio con membresía ${activa.plan} activa hasta ${fechaCorta(activa.fechaFin)}.`;
    }
  }

  cargarHistorial():void{
    this.cargandoHistorial=true;
    this.loans.historial().subscribe({
      next:data=>{this.historial=data;this.cargandoHistorial=false;},
      error:e=>{
        this.cargandoHistorial=false;
        this.showNotification('error','Historial',mensajeError(e,'No se pudo cargar el historial.'));
      }
    });
  }

  onRegisterLoan(data:LoanFormData):void{
    const unidades:UnidadPrestada[]=data.unidades.map(v=>{
      const [codigo,numSerie]=v.split('::');
      return {codigo,numSerie};
    }).filter(u=>u.codigo&&u.numSerie);

    if(!unidades.length){
      this.showNotification('error','Sin juegos','Agrega al menos una unidad disponible.');
      return;
    }
    if(data.fechaDevolucion<data.fechaSalida){
      this.showNotification('error','Fechas inválidas',
        'La fecha de devolución no puede ser anterior a la fecha de salida.');
      return;
    }

    this.submitting=true;
    this.loans.crear({
      fechaSalida:data.fechaSalida,
      fechaDevolucion:data.fechaDevolucion,
      estadoComponentes:'PENDIENTE_DEVOLUCION',
      tipoPrestamo:data.tipoPrestamo,
      documentoCliente:data.documentoCliente,
      documentoEmp:data.documentoEmpleado,
      codigoUnidad:unidades[0].codigo,
      numSerieUnidad:unidades[0].numSerie,
      unidades
    }).subscribe({
      next:r=>{
        this.submitting=false;
        this.loanForm?.resetLoan();
        this.showNotification('success','Préstamo registrado',
          `Préstamo #${r.idPrestamo} registrado con ${unidades.length} juego(s).`);
        this.clienteInfo='';this.clienteAviso=false;
        this.loadData();
      },
      error:e=>{
        this.submitting=false;
        this.showNotification('error','Error al registrar',
          mensajeError(e,'No se pudo registrar el préstamo.'));
      }
    });
  }

  onActiveLoanChange(value:string):void{
    this.activeLoanId=value;
    const p=this.activos.find(a=>String(a.idPrestamo)===value);
    this.activeLoanDetail=p
      ?`Salió el ${fechaCorta(p.fechaSalida)} (${this.etiquetaTipo(p.tipoPrestamo).toLowerCase()}). Juegos: `
        +this.unidadesDe(p).map(u=>`${this.nombresJuego[u.numSerie]||u.numSerie} (${u.codigo})`).join(', ')+'.'
      :'';
  }

  onUpdateAudit(data:{prestamoId:string;auditoria:LoanAuditData}):void{
    this.submitting=true;
    this.loans.devolver(Number(data.prestamoId),{
      fechaDevolucionReal:data.auditoria.fechaDevolucion,
      estadoComponentes:data.auditoria.estadoComponentes,
      observaciones:data.auditoria.observaciones
    }).subscribe({
      next:()=>{
        this.submitting=false;
        this.activeLoanId='';
        this.loanForm?.resetAudit();
        const destino=data.auditoria.estadoComponentes==='COMPLETO'
          ?'Los juegos quedaron disponibles.'
          :data.auditoria.estadoComponentes==='INCOMPLETO'
            ?'Los juegos quedaron como "Reportado con faltantes" hasta su revisión.'
            :'Los juegos quedaron en mantenimiento.';
        this.showNotification('success','Devolución registrada',destino);
        this.loadData();
      },
      error:e=>{
        this.submitting=false;
        this.showNotification('error','Error de devolución',
          mensajeError(e,'No se pudo completar la devolución.'));
      }
    });
  }

  onCancel():void{
    this.showNotification('info','Formulario restablecido','Puedes registrar otro préstamo.');
  }
  onRetry():void{this.loadData();}

  fechaCorta(valor:unknown):string{return fechaCorta(valor);}

  nombreCliente(doc:string):string{
    return this.clienteOptions.find(c=>c.value===doc)?.label||doc;
  }

  unidad(p:PrestamoResponse):string{
    const lista=this.unidadesDe(p);
    return lista.length
      ? lista.map(u=>`${this.nombresJuego[u.numSerie]||u.numSerie} (${u.codigo})`).join(', ')
      : '—';
  }

  /** El backend guarda la devolución como "DEVUELTO fecha | Estado: X | observaciones". */
  private estadoBase(estado:string):string{
    const texto=(estado||'').toUpperCase();
    const m=texto.match(/ESTADO:\s*([A-ZÑ_]+)/);
    return m ? m[1] : texto.trim();
  }

  etiquetaEstado(estado:string):string{
    const e=this.estadoBase(estado);
    if(e==='PENDIENTE_DEVOLUCION') return 'Pendiente de devolución';
    if(e==='COMPLETO') return 'Devuelto completo';
    if(e==='INCOMPLETO') return 'Devuelto incompleto';
    if(e==='DANADO'||e==='DAÑADO') return 'Devuelto dañado';
    return estado||'—';
  }

  claseEstado(estado:string):string{
    const e=this.estadoBase(estado);
    if(e==='PENDIENTE_DEVOLUCION') return 'badge--warning';
    if(e==='COMPLETO') return 'badge--success';
    return 'badge--danger';
  }

  etiquetaTipo(tipo:string):string{
    const t=(tipo||'').toUpperCase();
    return t==='INTERNO'?'Interno':t==='EXTERNO'?'Externo':(tipo||'—');
  }

  showNotification(type:NotificationType,title:string,message:string):void{
    this.notificationType=type;
    this.notificationTitle=title;
    this.notificationMessage=message;
    this.notificationVisible=true;
  }
  closeNotification():void{this.notificationVisible=false;}
}
