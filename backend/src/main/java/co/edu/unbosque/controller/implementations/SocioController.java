package co.edu.unbosque.controller.implementations;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import co.edu.unbosque.controller.interfaces.ISocioController;
import co.edu.unbosque.model.dto.SocioRequestDTO;
import co.edu.unbosque.model.dto.SocioResponseDTO;
import co.edu.unbosque.services.implementations.SocioService;
import co.edu.unbosque.services.interfaces.ISocioService;
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

@Path("/socio")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SocioController implements ISocioController {

    private final ISocioService socioService = new SocioService();

    @Override
    @POST
    public Response guardarSocio(SocioRequestDTO request) {
        SocioResponseDTO socio = socioService.addSocio(request);
        if (socio == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo registrar el socio")
                    .build();
        }
        return Response.status(Response.Status.CREATED)
                .entity(socio)
                .build();
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarPorFechaAfiliacion(
            @QueryParam("fechaAfiliacion") String fechaAfiliacion) {
        try {
            LocalDate fecha = LocalDate.parse(fechaAfiliacion);
            List<SocioResponseDTO> socios =
                    socioService.getSociosPorFechaAfiliacion(fecha);
            return Response.ok(socios).build();
        } catch (DateTimeParseException | NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La fecha debe tener el formato yyyy-MM-dd")
                    .build();
        }
    }

    @Override
    @GET
    @Path("/{documento}")
    public Response buscarPorDocumento(@PathParam("documento") String documento) {
        SocioResponseDTO socio = socioService.getSocioPorDocumento(documento);
        if (socio == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el socio")
                    .build();
        }
        return Response.ok(socio).build();
    }

    @Override
    @PUT
    @Path("/{documento}")
    public Response actualizarSocio(@PathParam("documento") String documento,
            SocioRequestDTO request) {
        SocioResponseDTO socio = socioService.updateSocio(documento, request);
        if (socio == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el socio que desea actualizar")
                    .build();
        }
        return Response.ok(socio).build();
    }

    @Override
    @DELETE
    @Path("/{documento}")
    public Response borrarSocio(@PathParam("documento") String documento) {
        SocioResponseDTO socio = socioService.deleteSocio(documento);
        if (socio == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el socio")
                    .build();
        }
        return Response.ok(socio).build();
    }

}