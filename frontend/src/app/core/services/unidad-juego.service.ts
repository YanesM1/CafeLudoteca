import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { UnidadJuegoDisponibilidad } from '../models/unidad-juego.model';

@Injectable({ providedIn: 'root' })
export class UnidadJuegoService {

    private readonly endpoint = '/api/unidadJuego';

    constructor(private readonly http: HttpClient) {}

    consultarDisponibilidad():
        Observable<UnidadJuegoDisponibilidad[]> {
        return this.http.get<UnidadJuegoDisponibilidad[]>(
            `${this.endpoint}/disponibilidad`
        );
    }
}
