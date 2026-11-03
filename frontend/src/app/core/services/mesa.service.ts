import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Mesa {
  idMesa: string;
  capacidad: number;
  tematica: string;
  ubicacion: string;
}

@Injectable({providedIn:'root'})
export class MesaService {
  private url='/api/mesa';
  constructor(private http:HttpClient){}
  listar():Observable<Mesa[]>{
    return this.http.get<Mesa[]>(`${this.url}/listar`);
  }
  disponibles(fecha:string, horario:string):Observable<Mesa[]> {
    const params = new HttpParams()
      .set('fecha', fecha)
      .set('horario', horario);
    return this.http.get<Mesa[]>(`${this.url}/disponibilidad`, {params});
  }

  buscar(tematica:string):Observable<Mesa[]>{
    const params=new HttpParams().set('tematica',tematica);
    return this.http.get<Mesa[]>(`${this.url}/buscar`,{params});
  }
  crear(data:Mesa):Observable<Mesa>{
    return this.http.post<Mesa>(this.url,data);
  }
  actualizar(id:string,data:Mesa):Observable<Mesa>{
    return this.http.put<Mesa>(`${this.url}/${encodeURIComponent(id)}`,data);
  }
  eliminar(id:string):Observable<Mesa>{
    return this.http.delete<Mesa>(`${this.url}/${encodeURIComponent(id)}`);
  }
}
