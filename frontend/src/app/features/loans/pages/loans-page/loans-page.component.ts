import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import {
  LoanFormComponent, LoanFormData, LoanAuditData
} from '../../../../shared/organisms/loan-form/loan-form.component';
import { SelectOption } from '../../../../shared/atoms/select/select.component';
import {
  NotificationToastComponent, NotificationType
} from '../../../../shared/molecules/notification-toast/notification-toast.component';
import { PrestamoService } from '../../../../core/services/prestamo.service';

@Component({
  selector:'app-loans-page',
  standalone:true,
  imports:[LoanFormComponent,NotificationToastComponent],
  templateUrl:'./loans-page.component.html',
  styleUrl:'./loans-page.component.css'
})
export class LoansPageComponent implements OnInit {
  clienteOptions:SelectOption[]=[];
  membershipOptions:SelectOption[]=[
    {value:'NO_APLICA',label:'No aplica: sin relación en la base de datos'}
  ];
  unitOptions:SelectOption[]=[];
  employeeOptions:SelectOption[]=[];
  componentStatusOptions:SelectOption[]=[
    {value:'COMPLETO',label:'Completo'},
    {value:'INCOMPLETO',label:'Incompleto'},
    {value:'DANADO',label:'Dañado'}
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

  constructor(private http:HttpClient,private loans:PrestamoService){}

  ngOnInit():void{this.loadData();}

  loadData():void{
    this.loading=true;
    this.errorMessage='';
    forkJoin({
      clients:this.http.get<any[]>('/api/cliente/buscar'),
      employees:this.http.get<any[]>('/api/empleado/listar'),
      units:this.http.get<any[]>('/api/unidadJuego/disponibles'),
      active:this.loans.activos()
    }).subscribe({
      next:r=>{
        this.clienteOptions=r.clients.map(c=>{
          const doc=c.documentoCliente||c.documento;
          return {
            value:doc,
            label:(c.primerNombre||'')+' '+(c.primerApellido||'')+' · '+doc
          };
        });
        this.employeeOptions=r.employees.map(e=>({
          value:e.documentoEmp,
          label:e.nombre+' '+e.apellido+' · '+e.documentoEmp
        }));
        this.unitOptions=r.units.map(u=>({
          value:u.codigo+'::'+u.numSerie,
          label:u.numSerie+' · Unidad '+u.codigo
        }));
        this.activeLoanOptions=r.active.map(p=>({
          value:String(p.idPrestamo),
          label:'Préstamo #'+p.idPrestamo+' · Cliente '+p.documentoCliente+
                ' · Unidad '+(p.codigoUnidad||'Sin unidad')
        }));
        this.loading=false;
      },
      error:e=>{
        this.loading=false;
        this.errorMessage=e.error?.mensaje||'Error cargando los datos operativos.';
      }
    });
  }

  onRegisterLoan(data:LoanFormData):void{
    const [codigoUnidad,numSerieUnidad]=(data.idUnidad||'').split('::');

    if(!codigoUnidad||!numSerieUnidad){
      this.showNotification('error','Unidad inválida','Selecciona una unidad disponible.');
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
      codigoUnidad,
      numSerieUnidad
    }).subscribe({
      next:r=>{
        this.submitting=false;
        this.showNotification('success','Préstamo registrado',
          'Préstamo #'+r.idPrestamo+' registrado correctamente.');
        this.loadData();
      },
      error:e=>{
        this.submitting=false;
        this.showNotification('error','Error al registrar',
          e.error?.mensaje||'No se pudo registrar el préstamo.');
      }
    });
  }

  onActiveLoanChange(value:string):void{this.activeLoanId=value;}

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
        this.showNotification('success','Devolución registrada',
          'Auditoría guardada y unidad liberada.');
        this.loadData();
      },
      error:e=>{
        this.submitting=false;
        this.showNotification('error','Error de devolución',
          e.error?.mensaje||'No se pudo completar la devolución.');
      }
    });
  }

  onCancel():void{
    this.showNotification('info','Formulario restablecido','Puedes registrar otro préstamo.');
  }
  onRetry():void{this.loadData();}

  showNotification(type:NotificationType,title:string,message:string):void{
    this.notificationType=type;
    this.notificationTitle=title;
    this.notificationMessage=message;
    this.notificationVisible=true;
  }
  closeNotification():void{this.notificationVisible=false;}
}
