
package co.edu.unbosque.controller.implementations;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.unbosque.controller.interfaces.IReservaController;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.dto.ReservaRequestDTO;
import co.edu.unbosque.model.dto.ReservaResponseDTO;
import co.edu.unbosque.services.implementations.ReservaService;
import co.edu.unbosque.services.interfaces.IReservaService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reservas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReservaController implements IReservaController {

    private static final Logger LOGGER =
            Logger.getLogger(ReservaController.class.getName());

    private final IReservaService reservaService =
            new ReservaService();

    // VALIDAR DATOS DE LA RESERVA
    private boolean validarReserva(
            ReservaRequestDTO request,
            boolean validarId) {

        if (request == null) {
            return false;
        }

        if (validarId &&
                (request.getIdReserva() == null ||
                 request.getIdReserva().isBlank())) {
            return false;
        }

        return request.getFecha() != null
                && request.getHorario() != null
                && !request.getHorario().isBlank()
                && request.getDocumento() != null
                && !request.getDocumento().isBlank()
                && request.getIdMesa() != null
                && !request.getIdMesa().isBlank();
    }

    // GENERAR RESPUESTAS DE ERROR EN JSON
    private Response respuestaError(
            Response.Status estado,
            String mensaje) {

        return Response.status(estado)
                .entity(Map.of("mensaje", mensaje))
                .build();
    }

    // MANEJAR ERRORES DE PERSISTENCIA
    private Response errorPersistencia(PersistenceException e) {

        LOGGER.log(Level.SEVERE,
                "Error de persistencia en el módulo de reservas", e);

        return respuestaError(
                Response.Status.INTERNAL_SERVER_ERROR,
                "Ocurrió un error al acceder a la base de datos.");
    }

    // REGISTRAR RESERVA
    @Override
    @POST
    public Response crearReserva(ReservaRequestDTO request) {

        if (!validarReserva(request, true)) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Los datos de la reserva son incompletos o inválidos.");
        }

        try {
            ReservaResponseDTO reserva =
                    reservaService.addReserva(request);

            if (reserva == null) {
                return respuestaError(
                        Response.Status.INTERNAL_SERVER_ERROR,
                        "No fue posible registrar la reserva.");
            }

            return Response.status(Response.Status.CREATED)
                    .entity(reserva)
                    .build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    // CONSULTAR RESERVAS POR DOCUMENTO DEL CLIENTE
    @Override
    @GET
    @Path("/buscar")
    public Response buscarReservas(
            @QueryParam("documento") String documento) {

        if (documento == null || documento.isBlank()) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Debes indicar el documento del cliente.");
        }

        try {
            List<ReservaResponseDTO> reservas =
                    reservaService.getReserva(documento.trim());

            return Response.ok(reservas).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    // CONSULTAR RESERVA POR IDENTIFICADOR
    @Override
    @GET
    @Path("/{idReserva}")
    public Response obtenerReserva(
            @PathParam("idReserva") String idReserva) {

        if (idReserva == null || idReserva.isBlank()) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Debes proporcionar el identificador de la reserva.");
        }

        try {
            ReservaResponseDTO reserva =
                    reservaService.getReservasPorIdReserva(idReserva);

            if (reserva == null) {
                return respuestaError(
                        Response.Status.NOT_FOUND,
                        "No se encontró la reserva.");
            }

            return Response.ok(reserva).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    // ACTUALIZAR RESERVA
    @Override
    @PUT
    @Path("/{idReserva}")
    public Response actualizarReserva(
            @PathParam("idReserva") String idReserva,
            ReservaRequestDTO request) {

        if (idReserva == null || idReserva.isBlank()
                || !validarReserva(request, false)) {

            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Los datos para actualizar la reserva son inválidos.");
        }

        if (request.getIdReserva() != null
                && !request.getIdReserva().isBlank()
                && !idReserva.equals(request.getIdReserva())) {

            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "No se permite modificar el identificador de la reserva.");
        }

        try {
            ReservaResponseDTO reserva =
                    reservaService.updateReserva(idReserva, request);

            if (reserva == null) {
                return respuestaError(
                        Response.Status.NOT_FOUND,
                        "No se encontró la reserva para actualizar.");
            }

            return Response.ok(reserva).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    // ELIMINAR RESERVA
    @Override
    @DELETE
    @Path("/{idReserva}")
    public Response eliminarReserva(
            @PathParam("idReserva") String idReserva) {

        if (idReserva == null || idReserva.isBlank()) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Debes proporcionar el identificador de la reserva.");
        }

        try {
            ReservaResponseDTO reserva =
                    reservaService.deleteReserva(idReserva);

            if (reserva == null) {
                return respuestaError(
                        Response.Status.NOT_FOUND,
                        "No se encontró la reserva.");
            }

            return Response.ok(reserva).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }
}
