export interface JuegoMesaResponse {
    numSerie: string;
    reglamento: string | null;
    cantidadMinimaJugadores: number;
    cantidadMaximaJugadores: number;
    edadMinima: number;
    nombre: string;
    duracionPartida: number;
}
