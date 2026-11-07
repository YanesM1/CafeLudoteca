package co.edu.unbosque.config;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.exceptions.PersistenceException;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Convierte cualquier excepción no controlada en una respuesta JSON
 * {"mensaje": "..."} con el código HTTP adecuado, en lugar de la página
 * HTML de error de WildFly.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER =
            Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable error) {

        if (error instanceof BusinessException negocio) {
            return ErrorResponses.of(negocio.getStatus(), negocio.getMessage());
        }

        if (error instanceof WebApplicationException web) {
            Response original = web.getResponse();
            int status = original != null ? original.getStatus() : 500;
            String mensaje = status == 404
                    ? "El recurso solicitado no existe."
                    : status < 500
                        ? "La solicitud no es válida. Revisa el formato de los datos enviados."
                        : "Ocurrió un error inesperado en el servidor.";
            return ErrorResponses.of(status, mensaje);
        }

        // Errores de MySQL con un mensaje claro según su código
        java.sql.SQLException sql = causaSql(error);
        if (sql != null) {
            switch (sql.getErrorCode()) {
                case 1062:
                    return ErrorResponses.of(Response.Status.CONFLICT,
                            "Ya existe un registro con esos datos.");
                case 1451:
                    return ErrorResponses.of(Response.Status.CONFLICT,
                            "No se puede eliminar porque tiene información relacionada "
                            + "(por ejemplo membresías, préstamos, reservas o unidades). Elimina primero esa información.");
                case 1452:
                    return ErrorResponses.of(Response.Status.CONFLICT,
                            "Uno de los datos hace referencia a un registro que no existe.");
                case 1048:
                    return ErrorResponses.of(Response.Status.BAD_REQUEST,
                            "Falta un dato obligatorio.");
                case 1406:
                    return ErrorResponses.of(Response.Status.BAD_REQUEST,
                            "Uno de los textos es demasiado largo para guardarse.");
                default:
                    break;
            }
        }

        if (tieneCausa(error, SQLIntegrityConstraintViolationException.class)) {
            LOGGER.log(Level.WARNING, "Violación de integridad", error);
            return ErrorResponses.of(Response.Status.CONFLICT,
                    "La operación viola una restricción de la base de datos: "
                    + "el registro ya existe, tiene datos relacionados o "
                    + "referencia un registro que no existe.");
        }

        if (error instanceof PersistenceException) {
            LOGGER.log(Level.SEVERE, "Error de persistencia", error);
            return ErrorResponses.of(Response.Status.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al acceder a la base de datos.");
        }

        LOGGER.log(Level.SEVERE, "Error no controlado", error);
        return ErrorResponses.of(Response.Status.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado en el servidor.");
    }

    private static java.sql.SQLException causaSql(Throwable error) {
        Throwable actual = error;
        while (actual != null) {
            if (actual instanceof java.sql.SQLException e && e.getErrorCode() != 0) {
                return e;
            }
            actual = actual.getCause();
        }
        return null;
    }

    private static boolean tieneCausa(Throwable error, Class<?> tipo) {
        Throwable actual = error;
        while (actual != null) {
            if (tipo.isInstance(actual)) {
                return true;
            }
            actual = actual.getCause();
        }
        return false;
    }
}
