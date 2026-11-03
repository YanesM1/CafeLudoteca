import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReservaRequest {
  idReserva: string;
  fecha: string;
  horario: string;
  documento: string;
  idMesa: string;
  juegos: string[];
}

export interface ReservaResponse {
  idReserva: string;
  fecha: string | number[];
  horario: string;
  documento: string;
  idMesa: string;
}

@Injectable({ providedIn: 'root' })
export class ReservaService {
  private readonly url = '/api/reservas';

  constructor(private http: HttpClient) {}

  crear(data: ReservaRequest): Observable<ReservaResponse> {
    return this.http.post<ReservaResponse>(this.url, data);
  }

  listar(documento: string): Observable<ReservaResponse[]> {
    const params = new HttpParams().set('documento', documento);
    return this.http.get<ReservaResponse[]>(`${this.url}/buscar`, { params });
  }

  buscar(id: string): Observable<ReservaResponse> {
    return this.http.get<ReservaResponse>(
      `${this.url}/${encodeURIComponent(id)}`
    );
  }

  actualizar(id: string, data: ReservaRequest): Observable<ReservaResponse> {
    return this.http.put<ReservaResponse>(
      `${this.url}/${encodeURIComponent(id)}`, data
    );
  }

  eliminar(id: string): Observable<ReservaResponse> {
    return this.http.delete<ReservaResponse>(
      `${this.url}/${encodeURIComponent(id)}`
    );
  }
}
