import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Categoria {
  numCategoria: number;
  categoria: string;
  numSerie: string;
}

@Injectable({providedIn:'root'})
export class CategoriaService {
  private url='/api/categoria';
  constructor(private http:HttpClient){}
  buscar(nombre:string):Observable<Categoria[]>{
    return this.http.get<Categoria[]>(`${this.url}/${encodeURIComponent(nombre)}`);
  }
  crear(data:Categoria):Observable<Categoria>{
    return this.http.post<Categoria>(this.url,data);
  }
  actualizar(id:number,serie:string,data:Categoria):Observable<Categoria>{
    return this.http.put<Categoria>(`${this.url}/${id}/${encodeURIComponent(serie)}`,data);
  }
  eliminar(id:number,serie:string):Observable<Categoria>{
    return this.http.delete<Categoria>(`${this.url}/${id}/${encodeURIComponent(serie)}`);
  }
}
