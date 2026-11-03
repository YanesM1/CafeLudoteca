import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PrestamoRequest {
  fechaSalida:string;
  fechaDevolucion:string;
  estadoComponentes:string;
  tipoPrestamo:string;
  documentoCliente:string;
  documentoEmp:string;
  codigoUnidad:string;
  numSerieUnidad:string;
}

@Injectable({providedIn:'root'})
export class PrestamoService {
  private url='/api/prestamo';
  constructor(private http:HttpClient){}
  crear(data:PrestamoRequest):Observable<any>{
    return this.http.post(this.url,data);
  }
  activos():Observable<any[]>{
    return this.http.get<any[]>(`${this.url}/activos`);
  }
  historial():Observable<any[]>{
    return this.http.get<any[]>(`${this.url}/historial`);
  }
  devolver(id:number,data:any):Observable<any>{
    return this.http.put(`${this.url}/${id}/devolucion`,data);
  }
}
