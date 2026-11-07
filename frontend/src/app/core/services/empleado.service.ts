import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { Empleado } from '../models/empleado.model';

@Injectable({ providedIn: 'root' })
export class EmpleadoService {
  private readonly endpoint = `${API_CONFIG.baseUrl}/empleado`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Empleado[]> {
    return this.http.get<Empleado[]>(`${this.endpoint}/listar`);
  }

  buscar(nombre: string): Observable<Empleado[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<Empleado[]>(`${this.endpoint}/buscar`, { params });
  }

  buscarDocumento(documento: string): Observable<Empleado> {
    return this.http.get<Empleado>(
      `${this.endpoint}/${encodeURIComponent(documento)}`
    );
  }

  crear(data: Empleado): Observable<Empleado> {
    return this.http.post<Empleado>(this.endpoint, data);
  }

  actualizar(documento: string, data: Empleado): Observable<Empleado> {
    return this.http.put<Empleado>(
      `${this.endpoint}/${encodeURIComponent(documento)}`, data
    );
  }

  eliminar(documento: string): Observable<Empleado> {
    return this.http.delete<Empleado>(
      `${this.endpoint}/${encodeURIComponent(documento)}`
    );
  }
}
