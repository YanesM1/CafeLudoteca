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
                                        .entity("No fue posible registrar el beneficio.")
                                        .build();
                }

                return Response.status(Response.Status.CREATED)
                                .entity(beneficio)
                                .build();
        }

        @Override
        @GET
        public Response obtenerBeneficios() {

                List<BeneficioResponseDTO> beneficios = beneficioService.getBeneficios();

                return Response.ok(beneficios).build();
        }

        @Override
        @GET
        @Path("/{idBeneficio}/{idMembresia}")
        public Response buscarPorId(
                        @PathParam("idBeneficio") Integer idBeneficio,
                        @PathParam("idMembresia") Integer idMembresia) {

                BeneficioResponseDTO beneficio = beneficioService.getBeneficioPorId(
                                idBeneficio,
                                idMembresia);

                if (beneficio == null) {
                        return Response.status(Response.Status.NOT_FOUND)
                                        .entity("Beneficio no encontrado.")
                                        .build();
                }

                return Response.ok(beneficio).build();
        }

        @Override
        @PUT
        @Path("/{idBeneficio}/{idMembresia}")
        public Response actualizarBeneficio(
                        @PathParam("idBeneficio") Integer idBeneficio,
                        @PathParam("idMembresia") Integer idMembresia,
                        BeneficioRequestDTO request) {

                BeneficioResponseDTO beneficio = beneficioService.updateBeneficio(
                                idBeneficio,
                                idMembresia,
                                request);

                if (beneficio == null) {
                        return Response.status(Response.Status.NOT_FOUND)
                                        .entity("Beneficio no encontrado.")
                                        .build();
                }

                return Response.ok(beneficio).build();
        }

        @Override
        @DELETE
        @Path("/{idBeneficio}/{idMembresia}")
        public Response borrarBeneficio(
                        @PathParam("idBeneficio") Integer idBeneficio,
                        @PathParam("idMembresia") Integer idMembresia) {

                BeneficioResponseDTO beneficio = beneficioService.deleteBeneficio(
                                idBeneficio,
                                idMembresia);

                if (beneficio == null) {
                        return Response.status(Response.Status.NOT_FOUND)
                                        .entity("Beneficio no encontrado.")
                                        .build();
                }

                return Response.ok(beneficio).build();
        }
}