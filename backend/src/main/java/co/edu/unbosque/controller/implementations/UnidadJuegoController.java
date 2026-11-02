package co.edu.unbosque.controller.implementations;
import co.edu.unbosque.controller.interfaces.IUnidadJuegoController;
import co.edu.unbosque.model.dto.UnidadJuegoRequestDTO;
import co.edu.unbosque.model.dto.UnidadJuegoResponseDTO;
import co.edu.unbosque.services.implementations.UnidadJuegoService;
import co.edu.unbosque.services.interfaces.IUnidadJuegoService;
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

@Path("/unidadJuego")
@Consumes (MediaType.APPLICATION_JSON)
@Produces (MediaType.APPLICATION_JSON)
public class UnidadJuegoController implements IUnidadJuegoController{
    private final IUnidadJuegoService unidadJuegoService=new UnidadJuegoService();



    @Override
    @POST 
    public Response guardarUnidad(UnidadJuegoRequestDTO request) {
       UnidadJuegoResponseDTO unidadJ=unidadJuegoService.addUnidadJuego(request);
        if(unidadJ==null){
            return Response.status(Response.Status.BAD_REQUEST)
            .entity("No se pudo registrar la unidad del juego")
            .build();
        }
        return Response.status(Response.Status.CREATED)
        .entity(unidadJ)
        .build();
    }

    @Override
    @GET 
    @Path("/{codigo}/{num_serie}")
    public Response buscarPorCodigo(
        @PathParam("codigo") String codigo, 
        @PathParam ("num_serie") String numSerie) {
        UnidadJuegoResponseDTO unidadJ=unidadJuegoService.findByCodigo(codigo, numSerie);
     if(unidadJ==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se ha encontrado el juego")
        .build();
     }
      return Response.ok(unidadJ).build();
  }

    

    @Override
    @PUT 
    @Path("/{codigo}/{num_serie}")
    public Response actualizarUnidadJuego(
        @PathParam ("codigo") String codigo,
        @PathParam ("num_serie") String numSerie, 
        UnidadJuegoRequestDTO request) {
        UnidadJuegoResponseDTO unidadJ=unidadJuegoService.updateUnidadJuego(codigo, numSerie, request);
      if(unidadJ==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se pudo actualizar la unidad de juego")
        .build();
      }
      return Response.ok(unidadJ).build();
    }

    @Override
     @DELETE 
     @Path("/{codigo}/{num_serie}")
    public Response borrarUnidadJuego(
        @PathParam ("codigo") String codigo, 
        @PathParam ("num_serie") String numSerie) {
       UnidadJuegoResponseDTO unidadJ=unidadJuegoService.deleteUnidadJuego(codigo, numSerie);
    if(unidadJ==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se pudo eliminar la unidad de la base de datos")
        .build();
    }
      return Response.ok(unidadJ).build();
    }
  



    @Override
    @GET
    @Path("/disponibilidad")
    public Response consultarDisponibilidad() {
        return Response.ok(
            unidadJuegoService.consultarDisponibilidad()
        ).build();
    }

}
