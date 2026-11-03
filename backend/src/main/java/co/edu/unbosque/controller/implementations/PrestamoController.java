
package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IPrestamoController;
import co.edu.unbosque.model.dto.PrestamoRequestDTO;
import co.edu.unbosque.model.dto.PrestamoResponseDTO;
import co.edu.unbosque.services.implementations.PrestamoServicio;
import co.edu.unbosque.services.interfaces.IPrestamoServicio;

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

@Path("/prestamo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PrestamoController implements IPrestamoController {

    private final IPrestamoServicio prestamoServicio = new PrestamoServicio();

    @Override
    @POST
    public Response guardarPrestamo(PrestamoRequestDTO request) {

        PrestamoResponseDTO prestamo = prestamoServicio.addPrestamo(request);

        if (prestamo == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo registrar el préstamo")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(prestamo)
                .build();
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarPorTipoPrestamo(
            @QueryParam("tipo_prestamo") String tipoPrestamo) {

        List<PrestamoResponseDTO> prestamos =
                prestamoServicio.getPrestamo(tipoPrestamo);

        return Response.ok(prestamos).build();
    }

    @Override
    @GET
    @Path("/buscar/{id_prestamo}")
    public Response buscarPorIdPrestamo(
            @PathParam("id_prestamo") Integer idPrestamo) {

        PrestamoResponseDTO prestamo =
                prestamoServicio.getPrestamoByIdPrestamo(idPrestamo);

        if (prestamo == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el préstamo")
                    .build();
        }

        return Response.ok(prestamo).build();
    }

    @Override
    @PUT
    @Path("/{id_prestamo}")
    public Response actualizarPrestamo(
            @PathParam("id_prestamo") Integer idPrestamo,
            PrestamoRequestDTO request) {

        PrestamoResponseDTO prestamo =
                prestamoServicio.updatePrestamo(idPrestamo, request);

        if (prestamo == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el préstamo que desea actualizar")
                    .build();
        }

        return Response.ok(prestamo).build();
    }

    @Override
    @DELETE
    @Path("/{id_prestamo}")
    public Response borrarPrestamo(
            @PathParam("id_prestamo") Integer idPrestamo) {

        PrestamoResponseDTO prestamo =
                prestamoServicio.deletePrestamo(idPrestamo);

        if (prestamo == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el préstamo que desea eliminar")
                    .build();
        }

        return Response.ok(prestamo).build();
    }
}
