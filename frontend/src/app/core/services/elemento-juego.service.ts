import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ElementoJuego {
  idElementoJuego?:number;
  elementoJuego:string;
  numSerie:string;
}

@Injectable({providedIn:'root'})
export class ElementoJuegoService {
  private url='/api/elementoJuego';
  constructor(private http:HttpClient){}
  /** Todos los elementos, o solo los del juego indicado. */
  listar(numSerie=''):Observable<ElementoJuego[]>{
    const params = numSerie.trim() ? { numSerie: numSerie.trim() } : undefined;
    return this.http.get<ElementoJuego[]>(this.url, { params });
  }

  buscarId(id:number):Observable<ElementoJuego>{
    return this.http.get<ElementoJuego>(`${this.url}/${id}`);
  }
  crear(data:ElementoJuego):Observable<ElementoJuego>{
    return this.http.post<ElementoJuego>(this.url,data);
  }
  actualizar(id:number,data:ElementoJuego):Observable<ElementoJuego>{
    return this.http.put<ElementoJuego>(`${this.url}/${id}`,data);
  }
  eliminar(id:number):Observable<ElementoJuego>{
    return this.http.delete<ElementoJuego>(`${this.url}/${id}`);
  }
}
