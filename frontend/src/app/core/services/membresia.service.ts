import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Membresia {
  idMembresia: number;
  plan: string;
  estado: string;
  fechaInicio: string | number[];
  fechaFin: string | number[];
  documento: string;
  tarifa: number;
}

/** Plan de membresía con su tarifa mensual (la define el backend). */
export interface PlanMembresia {
  plan: string;
  tarifa: number;
}

@Injectable({ providedIn: 'root' })
export class MembresiaService {
  private readonly endpoint = '/api/membresia';

  constructor(private http: HttpClient) {}

  planes(): Observable<PlanMembresia[]> {
    return this.http.get<PlanMembresia[]>(`${this.endpoint}/planes`);
  }

  listar(): Observable<Membresia[]> {
    return this.http.get<Membresia[]>(`${this.endpoint}/buscar`);
  }

  buscarPorDocumento(documento: string): Observable<Membresia[]> {
    const params = new HttpParams().set('documento', documento);
    return this.http.get<Membresia[]>(
      `${this.endpoint}/buscar/documento`, { params }
    );
  }

  crear(data: Membresia): Observable<Membresia> {
    return this.http.post<Membresia>(this.endpoint, data);
  }

  actualizar(id: number, documento: string, data: Membresia): Observable<Membresia> {
    return this.http.put<Membresia>(
      `${this.endpoint}/${id}/${encodeURIComponent(documento)}`, data
    );
  }

  eliminar(id: number, documento: string): Observable<Membresia> {
    return this.http.delete<Membresia>(
      `${this.endpoint}/${id}/${encodeURIComponent(documento)}`
    );
  }
}
