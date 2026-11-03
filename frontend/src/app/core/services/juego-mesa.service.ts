import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { JuegoMesaResponse } from '../models/juego-mesa.model';

@Injectable({ providedIn: 'root' })
export class JuegoMesaService {
  private readonly endpoint = `${API_CONFIG.baseUrl}/juegoMesa`;

  constructor(private readonly http: HttpClient) {}

  listar(nombre = ''): Observable<JuegoMesaResponse[]> {
    let params = new HttpParams();
    if (nombre.trim()) params = params.set('nombre', nombre.trim());
    return this.http.get<JuegoMesaResponse[]>(`${this.endpoint}/buscar`, { params });
  }

  buscarPorNumeroSerie(numSerie: string): Observable<JuegoMesaResponse> {
    return this.http.get<JuegoMesaResponse>(
      `${this.endpoint}/buscar/${encodeURIComponent(numSerie)}`
    );
  }

  crear(data: JuegoMesaResponse): Observable<JuegoMesaResponse> {
    return this.http.post<JuegoMesaResponse>(this.endpoint, data);
  }

  actualizar(numSerie: string, data: JuegoMesaResponse): Observable<JuegoMesaResponse> {
    return this.http.put<JuegoMesaResponse>(
      `${this.endpoint}/${encodeURIComponent(numSerie)}`, data
    );
  }

  eliminar(numSerie: string): Observable<void> {
    return this.http.delete<void>(
      `${this.endpoint}/${encodeURIComponent(numSerie)}`
    );
  }
}
