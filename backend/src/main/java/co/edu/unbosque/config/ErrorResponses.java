package co.edu.unbosque.config;

import java.util.Map;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Construye todas las respuestas de error con el mismo formato JSON:
 * {"mensaje": "..."}.
 */
public final class ErrorResponses {

    private ErrorResponses() {
    }

    public static Response of(int status, String mensaje) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("mensaje", mensaje))
                .build();
    }

    public static Response of(Response.Status status, String mensaje) {
        return of(status.getStatusCode(), mensaje);
    }
}
