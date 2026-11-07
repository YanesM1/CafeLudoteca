import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/** Unidad física incluida en un préstamo. */
export interface UnidadPrestada {
  codigo:string;
  numSerie:string;
}

export interface PrestamoRequest {
  fechaSalida:string;
  fechaDevolucion:string;
  estadoComponentes:string;
  tipoPrestamo:string;
  documentoCliente:string;
  documentoEmp:string;
  codigoUnidad:string;
  /** Todas las unidades del préstamo. */
  unidades?:UnidadPrestada[];
  numSerieUnidad:string;
}

export interface PrestamoResponse {
  idPrestamo:number;
  fechaSalida:string;
  fechaDevolucion:string;
  estadoComponentes:string;
  tipoPrestamo:string;
  documentoCliente:string;
  documentoEmp:string;
  codigoUnidad:string|null;
  unidades?:UnidadPrestada[];
  numSerieUnidad:string|null;
}

export interface DevolucionRequest {
  fechaDevolucionReal:string;
  estadoComponentes:string;
  observaciones:string;
}

@Injectable({providedIn:'root'})
export class PrestamoService {
  private url='/api/prestamo';
  constructor(private http:HttpClient){}
  crear(data:PrestamoRequest):Observable<PrestamoResponse>{
    return this.http.post<PrestamoResponse>(this.url,data);
  }
  activos():Observable<PrestamoResponse[]>{
    return this.http.get<PrestamoResponse[]>(`${this.url}/activos`);
  }
  historial():Observable<PrestamoResponse[]>{
    return this.http.get<PrestamoResponse[]>(`${this.url}/historial`);
  }
  devolver(id:number,data:DevolucionRequest):Observable<PrestamoResponse>{
    return this.http.put<PrestamoResponse>(`${this.url}/${id}/devolucion`,data);
  }
}
