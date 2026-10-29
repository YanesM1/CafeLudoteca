
package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IReservaController;
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

    private final IReservaService reservaService = new ReservaService();

    @Override
    @POST
    public Response crearReserva(ReservaRequestDTO request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Los datos de la reserva son obligatorios.")
                    .build();
        }

        ReservaResponseDTO reserva = reservaService.AddReserva(request);

        if (reserva == null) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No fue posible registrar la reserva.")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(reserva)
                .build();
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarReservas(
            @QueryParam("documento") String documento) {

        if (documento == null || documento.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Debes indicar el documento del cliente.")
                    .build();
        }

        List<ReservaResponseDTO> reservas =
                reservaService.getReserva(documento);

        return Response.ok(reservas).build();
    }

    @Override
    @GET
    @Path("/{idReserva}")
    public Response obtenerReserva(
            @PathParam("idReserva") String idReserva) {

        ReservaResponseDTO reserva =
                reservaService.getReservasPorIdReserva(idReserva);

        if (reserva == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró la reserva.")
                    .build();
        }

        return Response.ok(reserva).build();
    }

    @Override
    @PUT
    @Path("/{idReserva}")
    public Response actualizarReserva(
            @PathParam("idReserva") String idReserva,
            ReservaRequestDTO request) {

        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Los datos para actualizar son obligatorios.")
                    .build();
        }

        if (reservaService.getReservasPorIdReserva(idReserva) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró la reserva.")
                    .build();
        }

        ReservaResponseDTO reserva =
                reservaService.updateReserva(idReserva, request);

        if (reserva == null) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No fue posible actualizar la reserva.")
                    .build();
        }

        return Response.ok(reserva).build();
    }

    @Override
    @DELETE
    @Path("/{idReserva}")
    public Response eliminarReserva(
            @PathParam("idReserva") String idReserva) {

        if (reservaService.getReservasPorIdReserva(idReserva) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró la reserva.")
                    .build();
        }

        ReservaResponseDTO reserva =
                reservaService.deleteReserva(idReserva);

        if (reserva == null) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No fue posible eliminar la reserva.")
                    .build();
        }

        return Response.ok(reserva).build();
    }
}
