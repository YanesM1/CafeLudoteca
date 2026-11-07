package co.edu.unbosque.controller.implementations;

import co.edu.unbosque.config.ErrorResponses;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IBeneficioController;
import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;
import co.edu.unbosque.services.implementations.BeneficioService;
import co.edu.unbosque.services.interfaces.IBeneficioService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/beneficio")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BeneficioController implements IBeneficioController {
    private final IBeneficioService service = new BeneficioService();

    @POST
    @Override
    public Response guardarBeneficio(BeneficioRequestDTO request) {
        BeneficioResponseDTO r = service.addBeneficio(request);
        return r == null
            ? ErrorResponses.of(Response.Status.BAD_REQUEST, "Datos inválidos")
            : Response.status(Response.Status.CREATED).entity(r).build();
    }

    @GET
    @Override
    public Response obtenerBeneficios() {
        List<BeneficioResponseDTO> lista = service.getBeneficios();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{idBeneficio}/{idMembresia}/{documentoSocio}")
    @Override
    public Response buscarPorId(@PathParam("idBeneficio") Integer id,
            @PathParam("idMembresia") Integer membresia,
            @PathParam("documentoSocio") String documento) {
        BeneficioResponseDTO r = service.getBeneficioPorId(id, membresia, documento);
        return r == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(r).build();
    }

    @PUT
    @Path("/{idBeneficio}/{idMembresia}/{documentoSocio}")
    @Override
    public Response actualizarBeneficio(@PathParam("idBeneficio") Integer id,
            @PathParam("idMembresia") Integer membresia,
            @PathParam("documentoSocio") String documento,
            BeneficioRequestDTO request) {
        BeneficioResponseDTO r = service.updateBeneficio(id, membresia, documento, request);
        return r == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(r).build();
    }

    @DELETE
    @Path("/{idBeneficio}/{idMembresia}/{documentoSocio}")
    @Override
    public Response borrarBeneficio(@PathParam("idBeneficio") Integer id,
            @PathParam("idMembresia") Integer membresia,
            @PathParam("documentoSocio") String documento) {
        BeneficioResponseDTO r = service.deleteBeneficio(id, membresia, documento);
        return r == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(r).build();
    }
}
