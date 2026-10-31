
package co.edu.unbosque.controller.implementations;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.unbosque.controller.interfaces.IMesaController;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.dto.MesaRequestDTO;
import co.edu.unbosque.model.dto.MesaResponseDTO;
import co.edu.unbosque.services.implementations.MesaService;
import co.edu.unbosque.services.interfaces.IMesaService;

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

@Path("/mesas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MesaController implements IMesaController {

    private static final Logger LOGGER =
            Logger.getLogger(MesaController.class.getName());

    private final IMesaService mesaService =
            new MesaService();

    // VALIDAR DATOS DE LA MESA
    private boolean validarMesa(
            MesaRequestDTO request,
            boolean validarId) {

        if (request == null) {
            return false;
        }

        if (validarId &&
                (request.getIdMesa() == null ||
                 request.getIdMesa().isBlank())) {
            return false;
        }

        return request.getCapacidad() > 0
                && request.getTematica() != null
                && !request.getTematica().isBlank()
                && request.getUbicacion() != null
                && !request.getUbicacion().isBlank();
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
                "Error de persistencia en el módulo de mesas", e);

        return respuestaError(
                Response.Status.INTERNAL_SERVER_ERROR,
                "Ocurrió un error al acceder a la base de datos.");
    }

    // REGISTRAR MESA
    @Override
    @POST
    public Response crearMesa(MesaRequestDTO request) {

        if (!validarMesa(request, true)) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Los datos de la mesa son incompletos o inválidos.");
        }

        try {
            MesaResponseDTO mesa =
                    mesaService.addMesa(request);

            if (mesa == null) {
                return respuestaError(
                        Response.Status.INTERNAL_SERVER_ERROR,
                        "No fue posible registrar la mesa.");
            }

            return Response.status(Response.Status.CREATED)
                    .entity(mesa)
                    .build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    // BUSCAR MESAS POR TEMÁTICA
    @Override
    @GET
    @Path("/buscar")
    public Response buscarMesas(
            @QueryParam("tematica") String tematica) {

        if (tematica == null || tematica.isBlank()) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Debes indicar la temática que deseas buscar.");
        }

        try {
            List<MesaResponseDTO> mesas =
                    mesaService.getMesa(tematica.trim());

            return Response.ok(mesas).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    // CONSULTAR MESA POR IDENTIFICADOR
    @Override
    @GET
    @Path("/{idMesa}")
    public Response obtenerMesa(
            @PathParam("idMesa") String idMesa) {

        if (idMesa == null || idMesa.isBlank()) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Debes proporcionar el identificador de la mesa.");
        }

        try {
            MesaResponseDTO mesa =
                    mesaService.getMesasPorIdMesa(idMesa);

            if (mesa == null) {
                return respuestaError(
                        Response.Status.NOT_FOUND,
                        "No se encontró la mesa.");
            }

            return Response.ok(mesa).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    // ACTUALIZAR MESA
    @Override
    @PUT
    @Path("/{idMesa}")
    public Response actualizarMesa(
            @PathParam("idMesa") String idMesa,
            MesaRequestDTO request) {

        if (idMesa == null || idMesa.isBlank()
                || !validarMesa(request, false)) {

            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Los datos para actualizar la mesa son inválidos.");
        }

        if (request.getIdMesa() != null
                && !request.getIdMesa().isBlank()
                && !idMesa.equals(request.getIdMesa())) {

            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "No se permite modificar el identificador de la mesa.");
        }

        try {
            MesaResponseDTO mesa =
                    mesaService.updateMesa(idMesa, request);

            if (mesa == null) {
                return respuestaError(
                        Response.Status.NOT_FOUND,
                        "No se encontró la mesa para actualizar.");
            }

            return Response.ok(mesa).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    // ELIMINAR MESA
    @Override
    @DELETE
    @Path("/{idMesa}")
    public Response eliminarMesa(
            @PathParam("idMesa") String idMesa) {

        if (idMesa == null || idMesa.isBlank()) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Debes proporcionar el identificador de la mesa.");
        }

        try {
            MesaResponseDTO mesa =
                    mesaService.deleteMesa(idMesa);

            if (mesa == null) {
                return respuestaError(
                        Response.Status.NOT_FOUND,
                        "No se encontró la mesa.");
            }

            return Response.ok(mesa).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }
}
