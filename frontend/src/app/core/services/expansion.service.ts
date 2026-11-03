import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Expansion {
  idExpansion?:number;
  nombre:string;
  descripcion:string;
  contenidoAdicional:string;
  numSerie:string;
}

@Injectable({providedIn:'root'})
export class ExpansionService {
  private url='/api/expansion';
  constructor(private http:HttpClient){}
  buscar(nombre:string):Observable<Expansion[]>{
    return this.http.get<Expansion[]>(`${this.url}/buscar/${encodeURIComponent(nombre)}`);
  }
  buscarId(id:number):Observable<Expansion>{
    return this.http.get<Expansion>(`${this.url}/${id}`);
  }
  crear(data:Expansion):Observable<Expansion>{
    return this.http.post<Expansion>(this.url,data);
  }
  actualizar(id:number,data:Expansion):Observable<Expansion>{
    return this.http.put<Expansion>(`${this.url}/${id}`,data);
  }
  eliminar(id:number):Observable<Expansion>{
    return this.http.delete<Expansion>(`${this.url}/${id}`);
  }
}
