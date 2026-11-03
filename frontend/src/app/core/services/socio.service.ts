import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Socio {
  documento: string;
  fechaAfiliacion: string | number[];
}

@Injectable({ providedIn: 'root' })
export class SocioService {
  private readonly endpoint = '/api/socio';

  constructor(private http: HttpClient) {}

  listarPorFecha(fecha: string): Observable<Socio[]> {
    const params = new HttpParams().set('fechaAfiliacion', fecha);
    return this.http.get<Socio[]>(`${this.endpoint}/buscar`, { params });
  }

  buscar(documento: string): Observable<Socio> {
    return this.http.get<Socio>(
      `${this.endpoint}/${encodeURIComponent(documento)}`
    );
  }

  crear(socio: Socio): Observable<Socio> {
    return this.http.post<Socio>(this.endpoint, socio);
  }

  actualizar(documento: string, socio: Socio): Observable<Socio> {
    return this.http.put<Socio>(
      `${this.endpoint}/${encodeURIComponent(documento)}`, socio
    );
  }

  eliminar(documento: string): Observable<Socio> {
    return this.http.delete<Socio>(
      `${this.endpoint}/${encodeURIComponent(documento)}`
    );
  }
}
