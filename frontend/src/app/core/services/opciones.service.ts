import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { ClienteResponse } from '../models/cliente.model';
import { JuegoMesaResponse } from '../models/juego-mesa.model';

/** Opción de una lista desplegable. */
export interface Opcion {
  value: string;
  label: string;
}

/**
 * Listas para los desplegables de los formularios (clientes, socios, juegos),
 * para no escribir a mano documentos ni números de serie.
 */
@Injectable({ providedIn: 'root' })
export class OpcionesService {

  constructor(private http: HttpClient) {}

  private nombre(c: ClienteResponse): string {
    return [c.primerNombre, c.segundoNombre, c.primerApellido, c.segundoApellido]
      .filter(Boolean).join(' ');
  }

  /** Clientes: "Ana Pérez · 1001". */
  clientes(): Observable<Opcion[]> {
    return this.http.get<ClienteResponse[]>('/api/cliente/buscar').pipe(
      map(lista => lista
        .map(c => ({ value: c.documentoCliente, label: `${this.nombre(c)} · ${c.documentoCliente}` }))
        .sort((a, b) => a.label.localeCompare(b.label))),
      catchError(() => of([]))
    );
  }

  /** Mapa documento -> nombre del cliente. */
  nombresClientes(): Observable<Record<string, string>> {
    return this.http.get<ClienteResponse[]>('/api/cliente/buscar').pipe(
      map(lista => {
        const r: Record<string, string> = {};
        lista.forEach(c => r[c.documentoCliente] = [c.primerNombre, c.primerApellido].filter(Boolean).join(' '));
        return r;
      }),
      catchError(() => of({}))
    );
  }

  /** Juegos: "Catan · CATAN-CL-2024-112". */
  juegos(): Observable<Opcion[]> {
    return this.http.get<JuegoMesaResponse[]>('/api/juegoMesa/buscar').pipe(
      map(lista => lista
        .map(j => ({ value: j.numSerie, label: `${j.nombre} · ${j.numSerie}` }))
        .sort((a, b) => a.label.localeCompare(b.label))),
      catchError(() => of([]))
    );
  }
}
