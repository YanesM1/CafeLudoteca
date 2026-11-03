import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UnidadJuego {
  codigo:string;
  estado:string;
  observaciones:string;
  numSerie:string;
}

export interface UnidadDisponibilidad {
  numSerie:string;
  unidadesDisponibles:number;
}

@Injectable({providedIn:'root'})
export class UnidadJuegoService {
  private url='/api/unidadJuego';
  constructor(private http:HttpClient){}
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
