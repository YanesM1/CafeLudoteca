export interface ClienteRequest {
    documento: string;
    primerApellido: string;
    segundoApellido: string | null;
    primerNombre: string;
    segundoNombre: string | null;
    direccion: string | null;
    fechaNacimiento: string | null;
}

export interface ClienteResponse extends ClienteRequest {}
