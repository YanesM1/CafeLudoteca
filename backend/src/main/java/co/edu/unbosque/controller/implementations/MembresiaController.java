package co.edu.unbosque.controller.implementations;

import co.edu.unbosque.controller.interfaces.IMembresiaController;
import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import co.edu.unbosque.services.implementations.MembresiaService;
import co.edu.unbosque.services.interfaces.IMembresiaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/membresia")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MembresiaController implements IMembresiaController {
    private final IMembresiaService service = new MembresiaService();

    @POST
    @Override
    public Response guardarMembresia(MembresiaRequestDTO request) {
        var r = service.addMembresia(request);
        return r == null
            ? Response.status(Response.Status.BAD_REQUEST).entity("{\"mensaje\":\"Datos inválidos o socio inexistente\"}").build()
            : Response.status(Response.Status.CREATED).entity(r).build();
    }

    @GET
    @Path("/buscar")
    @Override
    public Response listar() {
        return Response.ok(service.getTodas()).build();
    }

    @GET
    @Path("/buscar/documento")
    @Override
    public Response buscarPorDocumento(@QueryParam("documento") String documento) {
        if (documento == null || documento.isBlank())
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"mensaje\":\"Documento obligatorio\"}").build();
        return Response.ok(service.getMembresiaPorDocumento(documento)).build();
    }

    @GET
    @Path("/buscar/estado")
    @Override
    public Response buscarPorEstado(@QueryParam("estado") String estado) {
        if (estado == null || estado.isBlank())
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"mensaje\":\"Estado obligatorio\"}").build();
        return Response.ok(service.getMembresiaPorEstado(estado)).build();
    }

    @GET
    @Path("/{idMembresia}/{documento}")
    @Override
    public Response buscarPorId(@PathParam("idMembresia") int id, @PathParam("documento") String documento) {
        var r = service.getMembresiaPorId(id, documento);
        return r == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(r).build();
    }

    @PUT
    @Path("/{idMembresia}/{documento}")
    @Override
    public Response actualizarMembresia(@PathParam("idMembresia") int id,
            @PathParam("documento") String documento, MembresiaRequestDTO request) {
        var r = service.updateMembresia(id, documento, request);
        return r == null ? Response.status(Response.Status.NOT_FOUND).entity("{\"mensaje\":\"Membresía no encontrada o datos inválidos\"}").build() : Response.ok(r).build();
    }

    @DELETE
    @Path("/{idMembresia}/{documento}")
    @Override
    public Response borrarMembresia(@PathParam("idMembresia") int id, @PathParam("documento") String documento) {
        var r = service.deleteMembresia(id, documento);
        return r == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(r).build();
    }
}
