package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IBeneficioController;
import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;
import co.edu.unbosque.services.implementations.BeneficioService;
import co.edu.unbosque.services.interfaces.IBeneficioService;
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

@Path("/beneficio")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BeneficioController implements IBeneficioController {

    private final IBeneficioService beneficioService = new BeneficioService();

    @Override
    @POST
    public Response guardarBeneficio(BeneficioRequestDTO request) {
        BeneficioResponseDTO beneficio = beneficioService.addBeneficio(request);
        if (beneficio == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo registrar el beneficio")
                    .build();
        }
        return Response.status(Response.Status.CREATED)
                .entity(beneficio)
                .build();
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarPorMembresia(@QueryParam("idMembresia") String idMembresia) {
        List<BeneficioResponseDTO> beneficios =
                beneficioService.getBeneficiosPorMembresia(idMembresia);
        return Response.ok(beneficios).build();
    }

    @Override
    @GET
    @Path("/{idMembresia}/{idBeneficio}")
    public Response buscarPorId(@PathParam("idMembresia") String idMembresia,
            @PathParam("idBeneficio") String idBeneficio) {
        BeneficioResponseDTO beneficio =
                beneficioService.getBeneficioPorId(idMembresia, idBeneficio);
        if (beneficio == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el beneficio")
                    .build();
        }
        return Response.ok(beneficio).build();
    }

    @Override
    @PUT
    @Path("/{idMembresia}/{idBeneficio}")
    public Response actualizarBeneficio(@PathParam("idMembresia") String idMembresia,
            @PathParam("idBeneficio") String idBeneficio,
            BeneficioRequestDTO request) {
        BeneficioResponseDTO beneficio =
                beneficioService.updateBeneficio(idMembresia, idBeneficio, request);
        if (beneficio == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el beneficio que desea actualizar")
                    .build();
        }
        return Response.ok(beneficio).build();
    }

    @Override
    @DELETE
    @Path("/{idMembresia}/{idBeneficio}")
    public Response borrarBeneficio(@PathParam("idMembresia") String idMembresia,
            @PathParam("idBeneficio") String idBeneficio) {
        BeneficioResponseDTO beneficio =
                beneficioService.deleteBeneficio(idMembresia, idBeneficio);
        if (beneficio == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el beneficio")
                    .build();
        }
        return Response.ok(beneficio).build();
    }

}