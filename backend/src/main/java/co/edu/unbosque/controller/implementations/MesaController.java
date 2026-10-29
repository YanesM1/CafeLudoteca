
package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IMesaController;
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

    private final IMesaService mesaService = new MesaService();

    @Override
    @POST
    public Response crearMesa(MesaRequestDTO request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Los datos de la mesa son obligatorios.")
                    .build();
        }

        MesaResponseDTO mesa = mesaService.AddMesa(request);

        if (mesa == null) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No fue posible registrar la mesa.")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(mesa)
                .build();
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarMesas(
            @QueryParam("tematica") String tematica) {

        if (tematica == null || tematica.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Debes indicar la temática que deseas buscar.")
                    .build();
        }

        List<MesaResponseDTO> mesas = mesaService.getMesa(tematica);

        return Response.ok(mesas).build();
    }

    @Override
    @GET
    @Path("/{idMesa}")
    public Response obtenerMesa(
            @PathParam("idMesa") String idMesa) {

        MesaResponseDTO mesa = mesaService.getMesasPorIdMesa(idMesa);

        if (mesa == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró la mesa.")
                    .build();
        }

        return Response.ok(mesa).build();
    }

    @Override
    @PUT
    @Path("/{idMesa}")
    public Response actualizarMesa(
            @PathParam("idMesa") String idMesa,
            MesaRequestDTO request) {

        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Los datos para actualizar son obligatorios.")
                    .build();
        }

        if (mesaService.getMesasPorIdMesa(idMesa) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró la mesa.")
                    .build();
        }

        MesaResponseDTO mesa = mesaService.updateMesa(idMesa, request);

        if (mesa == null) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No fue posible actualizar la mesa.")
                    .build();
        }

        return Response.ok(mesa).build();
    }

    @Override
    @DELETE
    @Path("/{idMesa}")
    public Response eliminarMesa(
            @PathParam("idMesa") String idMesa) {

        if (mesaService.getMesasPorIdMesa(idMesa) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró la mesa.")
                    .build();
        }

        MesaResponseDTO mesa = mesaService.deleteMesa(idMesa);

        if (mesa == null) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No fue posible eliminar la mesa.")
                    .build();
        }

        return Response.ok(mesa).build();
    }
}
