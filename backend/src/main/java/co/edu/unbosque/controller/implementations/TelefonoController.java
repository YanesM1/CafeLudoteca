package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.ITelefonoController;
import co.edu.unbosque.model.dto.TelefonoRequestDTO;
import co.edu.unbosque.model.dto.TelefonoResponseDTO;
import co.edu.unbosque.services.implementations.TelefonoService;
import co.edu.unbosque.services.interfaces.ITelefonoService;
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

@Path("/telefono")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TelefonoController implements ITelefonoController {

    private final ITelefonoService telefonoService = new TelefonoService();

    @Override
    @POST
    public Response guardarTelefono(TelefonoRequestDTO request) {
        TelefonoResponseDTO telefono = telefonoService.addTelefono(request);
        if (telefono == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo registrar el telefono")
                    .build();
        }
        return Response.status(Response.Status.CREATED)
                .entity(telefono)
                .build();
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarPorDocumento(@QueryParam("documento_cliente") String documentoCliente) {
        List<TelefonoResponseDTO> telefonos =
                telefonoService.getTelefonosPorDocumento(documentoCliente);
        return Response.ok(telefonos).build();
    }

   

    @Override
    @PUT
    @Path("/{documento_cliente}/{telefono}")
    public Response actualizarTelefono(@PathParam("documento_cliente") String documentoCliente,
            @PathParam("telefono") String telefono,
            TelefonoRequestDTO request) {
        TelefonoResponseDTO telefonoC =
                telefonoService.updateTelefono(documentoCliente, telefono, request);
        if (telefonoC == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el telefono que desea actualizar")
                    .build();
        }
        return Response.ok(telefonoC).build();
    }

    @Override
    @DELETE
    @Path("/{documento_cliente}/{telefono}")
    public Response borrarTelefono(@PathParam("documento_cliente") String documentoCliente,
            @PathParam("telefono") String telefono) {
        TelefonoResponseDTO telefonoC =
                telefonoService.deleteTelefono(documentoCliente, telefono);
        if (telefonoC == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se ha encontrado el telefono")
                    .build();
        }
        return Response.ok(telefonoC).build();
    }
}
