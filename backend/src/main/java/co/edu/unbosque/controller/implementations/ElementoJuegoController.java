
package co.edu.unbosque.controller.implementations;

import co.edu.unbosque.config.ErrorResponses;

import co.edu.unbosque.controller.interfaces.IElementoJuegoController;
import co.edu.unbosque.model.dto.ElementoJuegoRequestDTO;
import co.edu.unbosque.model.dto.ElementoJuegoResponseDTO;
import co.edu.unbosque.services.implementations.ElementoJuegoService;
import co.edu.unbosque.services.interfaces.IElementoJuegoService;

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

@Path("/elementoJuego")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElementoJuegoController implements IElementoJuegoController {

    private final IElementoJuegoService elementoJuegoService = new ElementoJuegoService();

    @Override
    @POST
    public Response guardarElementoJuego(ElementoJuegoRequestDTO request) {

        ElementoJuegoResponseDTO elementoJuego =
                elementoJuegoService.addElementoJuego(request);

        if (elementoJuego == null) {
            return ErrorResponses.of(Response.Status.BAD_REQUEST, "No se pudo registrar el elemento de juego");
        }

        return Response.status(Response.Status.CREATED)
                .entity(elementoJuego)
                .build();
    }

    @Override
    @GET
    @Path("/{idElementoJuego}")
    public Response buscarPorIdElementoJuego(
            @PathParam("idElementoJuego") Integer idElementoJuego) {

        ElementoJuegoResponseDTO elementoJuego =
                elementoJuegoService.getPorIdElementoJuego(idElementoJuego);

        if (elementoJuego == null) {
            return ErrorResponses.of(Response.Status.NOT_FOUND, "No se ha encontrado el elemento de juego");
        }

        return Response.ok(elementoJuego).build();
    }

    @Override
    @PUT
    @Path("/{idElementoJuego}")
    public Response actualizarElementoJuego(
            @PathParam("idElementoJuego") Integer idElementoJuego,
            ElementoJuegoRequestDTO request) {

        ElementoJuegoResponseDTO elementoJuego =
                elementoJuegoService.updateElementoJuego(idElementoJuego, request);

        if (elementoJuego == null) {
            return ErrorResponses.of(Response.Status.NOT_FOUND, "No se ha encontrado el elemento de juego que desea actualizar");
        }

        return Response.ok(elementoJuego).build();
    }

    @Override
    @DELETE
    @Path("/{idElementoJuego}")
    public Response borrarElementoJuego(
            @PathParam("idElementoJuego") Integer idElementoJuego) {

        ElementoJuegoResponseDTO elementoJuego =
                elementoJuegoService.deleteElementoJuego(idElementoJuego);

        if (elementoJuego == null) {
            return ErrorResponses.of(Response.Status.NOT_FOUND, "No se ha encontrado el elemento de juego");
        }

        return Response.ok(elementoJuego).build();
    }

    // LISTAR ELEMENTOS (opcionalmente los de un juego con ?numSerie=)
    @GET
    public Response listarElementos(@QueryParam("numSerie") String numSerie) {
        return Response.ok(elementoJuegoService.listar(numSerie)).build();
    }
}
