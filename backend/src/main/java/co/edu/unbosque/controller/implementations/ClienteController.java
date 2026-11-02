package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IClienteController;
import co.edu.unbosque.model.dto.ClienteRequestDTO;
import co.edu.unbosque.model.dto.ClienteResponseDTO;
import co.edu.unbosque.services.implementations.ClienteService;
import co.edu.unbosque.services.interfaces.IClienteService;
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

@Path("/cliente")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteController implements IClienteController {

    private final IClienteService clienteService = new ClienteService();

    @Override
    @POST
    public Response guardarCliente(ClienteRequestDTO request) {
        ClienteResponseDTO cliente = clienteService.addCliente(request);
        if (cliente == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo registrar el cliente")
                    .build();
        }
        return Response.status(Response.Status.CREATED)
                .entity(cliente)
                .build();
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarPorNombre(@QueryParam("nombre") String nombre) {
        List<ClienteResponseDTO> clientes = clienteService.getCliente(nombre);
        return Response.ok(clientes).build();
    }

    @Override
    @GET
    @Path("/{documento}")
    public Response buscarPorDocumento(@PathParam("documento") String documento) {
        ClienteResponseDTO cliente = clienteService.getClientePorDocumento(documento);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el cliente")
                    .build();
        }
        return Response.ok(cliente).build();
    }

    @Override
    @PUT
    @Path("/{documento}")
    public Response actualizarCliente(@PathParam("documento") String documento,
            ClienteRequestDTO request) {
        ClienteResponseDTO cliente = clienteService.updateCliente(documento, request);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el cliente que desea actualizar")
                    .build();
        }
        return Response.ok(cliente).build();
    }

    @Override
    @DELETE
    @Path("/{documento}")
    public Response borrarCliente(@PathParam("documento") String documento) {
        ClienteResponseDTO cliente = clienteService.deleteCliente(documento);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el cliente")
                    .build();
        }
        return Response.ok(cliente).build();
    }

}