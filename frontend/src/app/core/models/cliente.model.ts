// Los nombres de los campos coinciden con ClienteRequestDTO/ClienteResponseDTO del backend.
export interface ClienteRequest {
    documentoCliente: string;
    primerApellido: string;
    segundoApellido: string | null;
    primerNombre: string;
    segundoNombre: string | null;
    direccion: string | null;
    fechaNacimiento: string | null;
}

export interface ClienteResponse extends ClienteRequest {}
