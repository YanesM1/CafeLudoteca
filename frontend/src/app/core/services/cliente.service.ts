import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClienteRequest, ClienteResponse } from '../models/cliente.model';

@Injectable({ providedIn: 'root' })
export class ClienteService {
    private readonly endpoint = '/api/cliente';

    constructor(private http: HttpClient) {}

    listar(nombre = ''): Observable<ClienteResponse[]> {
        let params = new HttpParams();

        if (nombre.trim()) {
            params = params.set('primer_nombre', nombre.trim());
        }

        return this.http.get<ClienteResponse[]>(
            `${this.endpoint}/buscar`, { params }
        );
    }

    buscarPorDocumento(documento: string): Observable<ClienteResponse> {
        return this.http.get<ClienteResponse>(
            `${this.endpoint}/buscar/${encodeURIComponent(documento)}`
        );
    }

    crear(cliente: ClienteRequest): Observable<ClienteResponse> {
        return this.http.post<ClienteResponse>(this.endpoint, cliente);
    }

    actualizar(documento: string, cliente: ClienteRequest):
        Observable<ClienteResponse> {
        return this.http.put<ClienteResponse>(
            `${this.endpoint}/${encodeURIComponent(documento)}`, cliente
        );
    }

    eliminar(documento: string): Observable<ClienteResponse> {
        return this.http.delete<ClienteResponse>(
            `${this.endpoint}/delete/${encodeURIComponent(documento)}`
        );
    }
}
