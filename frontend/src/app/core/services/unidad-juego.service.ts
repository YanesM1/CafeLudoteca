import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UnidadJuego {
  codigo:string;
  estado:string;
  observaciones:string;
  numSerie:string;
}

/** Estados válidos de una unidad física (coinciden con el backend). */
export const ESTADOS_UNIDAD = [
  { value: 'DISPONIBLE', label: 'Disponible' },
  { value: 'EN_MESA', label: 'En uso en mesa' },
  { value: 'PRESTADO', label: 'Prestado a domicilio' },
  { value: 'MANTENIMIENTO', label: 'En mantenimiento' },
  { value: 'FALTANTES', label: 'Reportado con faltantes' }
] as const;

/** Lleva cualquier variante guardada ("Disponible", "en mesa"...) al valor estándar. */
export function normalizarEstadoUnidad(estado: string | null | undefined): string {
  const valor = (estado || '').trim().toUpperCase().replace(/ /g, '_');
  switch (valor) {
    case 'EN_USO': case 'EN_USO_EN_MESA': return 'EN_MESA';
    case 'REPORTADO_CON_FALTANTES': case 'INCOMPLETO': return 'FALTANTES';
    case 'DAÑADO': case 'DANADO': case 'EN_REPARACION': case 'EN_REPARACIÓN': return 'MANTENIMIENTO';
    default: return valor;
  }
}

export function etiquetaEstadoUnidad(estado: string | null | undefined): string {
  const valor = normalizarEstadoUnidad(estado);
  return ESTADOS_UNIDAD.find(e => e.value === valor)?.label ?? (estado || '');
}

export interface UnidadDisponibilidad {
  numSerie:string;
  unidadesDisponibles:number;
}

@Injectable({providedIn:'root'})
export class UnidadJuegoService {
  private url='/api/unidadJuego';
  constructor(private http:HttpClient){}
  /** Todas las unidades, o solo las del juego indicado. */
  listar(numSerie=''):Observable<UnidadJuego[]>{
    const params = numSerie.trim() ? { numSerie: numSerie.trim() } : undefined;
    return this.http.get<UnidadJuego[]>(this.url, { params });
  }

  consultarDisponibilidad():Observable<UnidadDisponibilidad[]>{
    return this.http.get<UnidadDisponibilidad[]>(`${this.url}/disponibilidad`);
  }
  buscar(codigo:string,serie:string):Observable<UnidadJuego>{
    return this.http.get<UnidadJuego>(`${this.url}/${encodeURIComponent(codigo)}/${encodeURIComponent(serie)}`);
  }
  crear(data:UnidadJuego):Observable<UnidadJuego>{
    return this.http.post<UnidadJuego>(this.url,data);
  }
  actualizar(codigo:string,serie:string,data:UnidadJuego):Observable<UnidadJuego>{
    return this.http.put<UnidadJuego>(`${this.url}/${encodeURIComponent(codigo)}/${encodeURIComponent(serie)}`,data);
  }
  eliminar(codigo:string,serie:string):Observable<UnidadJuego>{
    return this.http.delete<UnidadJuego>(`${this.url}/${encodeURIComponent(codigo)}/${encodeURIComponent(serie)}`);
  }
}
