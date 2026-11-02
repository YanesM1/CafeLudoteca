package co.edu.unbosque.controller.implementations;

import co.edu.unbosque.controller.interfaces.IMembresiaController;
import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import co.edu.unbosque.model.dto.MembresiaResponseDTO;
import co.edu.unbosque.services.implementations.MembresiaService;
import co.edu.unbosque.services.interfaces.IMembresiaService;
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
import lombok.NoArgsConstructor;

@Path("/membresia")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@NoArgsConstructor
public class MembresiaController implements IMembresiaController {

    private IMembresiaService membresiaService = new MembresiaService();

    @Override
    @POST
    public Response guardarMembresia(MembresiaRequestDTO request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La solicitud no puede ser nula")
                    .build();
        }

        MembresiaResponseDTO membresia = membresiaService.addMembresia(request);

        if (membresia == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo registrar la membresía")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(membresia)
                .build();
    }

    @Override
    @GET
    @Path("/buscar/documento")
    public Response buscarPorDocumento(@QueryParam("documento") String documento) {
        if (documento == null || documento.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El documento es obligatorio")
                    .build();
        }

        return Response.ok(
                membresiaService.getMembresiaPorDocumento(documento.trim()))
                .build();
    }

    @Override
    @GET
    @Path("/buscar/estado")
    public Response buscarPorEstado(@QueryParam("estado") String estado) {
        if (estado == null || estado.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El estado es obligatorio")
                    .build();
        }

        return Response.ok(
                membresiaService.getMembresiaPorEstado(estado.trim()))
                .build();

    }

    @Override
    @GET
    @Path("/{idMembresia}")
    public Response buscarPorId(@PathParam("idMembresia") int idMembresia) {
        if (idMembresia <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El id debe ser mayor que cero")
                    .build();
        }

        MembresiaResponseDTO membresia = membresiaService.getMembresiaPorId(idMembresia);

        if (membresia == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado la membresía")
                    .build();
        }

        return Response.ok(membresia).build();
    }

    @Override
    @PUT
    @Path("/{idMembresia}")
    public Response actualizarMembresia(@PathParam("idMembresia") int idMembresia,
            MembresiaRequestDTO request) {
        if (idMembresia <= 0 || request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Id y solicitud son obligatorios")
                    .build();
        }

        MembresiaResponseDTO membresia = membresiaService.updateMembresia(idMembresia, request);

        if (membresia == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado la membresía que desea actualizar")
                    .build();
        }

        return Response.ok(membresia).build();
    }

    @Override
    @DELETE
    @Path("/{idMembresia}")
    public Response borrarMembresia(@PathParam("idMembresia") int idMembresia) {
        if (idMembresia <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El id debe ser mayor que cero")
                    .build();
        }

        MembresiaResponseDTO membresia = membresiaService.deleteMembresia(idMembresia);

        if (membresia == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado la membresía")
                    .build();
        }

        return Response.ok(membresia).build();
    }
}
