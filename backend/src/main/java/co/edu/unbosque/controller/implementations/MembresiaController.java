package co.edu.unbosque.controller.implementations;

import java.util.List;

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

@Path("/membresia")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MembresiaController implements IMembresiaController {

    private final IMembresiaService membresiaService = new MembresiaService();

    @Override
    @POST
    public Response guardarMembresia(MembresiaRequestDTO request) {
        MembresiaResponseDTO membresia = membresiaService.addMembresia(request);
        if (membresia == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo registrar la membresia")
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
        List<MembresiaResponseDTO> membresias =
                membresiaService.getMembresiasPorDocumento(documento);
        return Response.ok(membresias).build();
    }

    @Override
    @GET
    @Path("/buscar/estado")
    public Response buscarPorEstado(@QueryParam("estado") String estado) {
        List<MembresiaResponseDTO> membresias =
                membresiaService.getMembresiasPorEstado(estado);
        return Response.ok(membresias).build();
    }

    @Override
    @GET
    @Path("/{idMembresia}")
    public Response buscarPorId(@PathParam("idMembresia") String idMembresia) {
        MembresiaResponseDTO membresia = membresiaService.getMembresiaPorId(idMembresia);
        if (membresia == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado la membresia")
                    .build();
        }
        return Response.ok(membresia).build();
    }

    @Override
    @PUT
    @Path("/{idMembresia}")
    public Response actualizarMembresia(@PathParam("idMembresia") String idMembresia,
            MembresiaRequestDTO request) {
        MembresiaResponseDTO membresia =
                membresiaService.updateMembresia(idMembresia, request);
        if (membresia == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado la membresia que desea actualizar")
                    .build();
        }
        return Response.ok(membresia).build();
    }

    @Override
    @DELETE
    @Path("/{idMembresia}")
    public Response borrarMembresia(@PathParam("idMembresia") String idMembresia) {
        MembresiaResponseDTO membresia = membresiaService.deleteMembresia(idMembresia);
        if (membresia == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado la membresia")
                    .build();
        }
        return Response.ok(membresia).build();
    }

}