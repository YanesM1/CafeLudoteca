package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IClienteFrecuenteController;
import co.edu.unbosque.model.dto.ClienteFrecuenteRequestDTO;
import co.edu.unbosque.model.dto.ClienteFrecuenteResponseDTO;
import co.edu.unbosque.services.implementations.ClienteFrecuenteService;
import co.edu.unbosque.services.interfaces.IClienteFrecuenteService;
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

@Path("/clienteFrecuente")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteFrecuenteController implements IClienteFrecuenteController {

    private final IClienteFrecuenteService clienteFrecuenteService =
            new ClienteFrecuenteService();

    @Override
    @POST
    public Response guardarClienteFrecuente(ClienteFrecuenteRequestDTO request) {
        ClienteFrecuenteResponseDTO clienteFrecuente =
                clienteFrecuenteService.addClienteFrecuente(request);
        if (clienteFrecuente == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo registrar el cliente frecuente")
                    .build();
        }
        return Response.status(Response.Status.CREATED)
                .entity(clienteFrecuente)
                .build();
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarPorNivelFidelidad(
            @QueryParam("nivelFidelidad") String nivelFidelidad) {
        List<ClienteFrecuenteResponseDTO> clientesFrecuentes =
                clienteFrecuenteService.getClientesFrecuentesPorNivel(nivelFidelidad);
        return Response.ok(clientesFrecuentes).build();
    }

    @Override
    @GET
    @Path("/{documento}")
    public Response buscarPorDocumento(@PathParam("documento") String documento) {
        ClienteFrecuenteResponseDTO clienteFrecuente =
                clienteFrecuenteService.getClienteFrecuentePorDocumento(documento);
        if (clienteFrecuente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el cliente frecuente")
                    .build();
        }
        return Response.ok(clienteFrecuente).build();
    }

    @Override
    @PUT
    @Path("/{documento}")
    public Response actualizarClienteFrecuente(@PathParam("documento") String documento,
            ClienteFrecuenteRequestDTO request) {
        ClienteFrecuenteResponseDTO clienteFrecuente =
                clienteFrecuenteService.updateClienteFrecuente(documento, request);
        if (clienteFrecuente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el cliente frecuente que desea actualizar")
                    .build();
        }
        return Response.ok(clienteFrecuente).build();
    }

    @Override
    @DELETE
    @Path("/{documento}")
    public Response borrarClienteFrecuente(@PathParam("documento") String documento) {
        ClienteFrecuenteResponseDTO clienteFrecuente =
                clienteFrecuenteService.deleteClienteFrecuente(documento);
        if (clienteFrecuente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el cliente frecuente")
                    .build();
        }
        return Response.ok(clienteFrecuente).build();
    }

}