package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IJuegoMesaController;
import co.edu.unbosque.model.dto.JuegoMesaRequestDTO;
import co.edu.unbosque.model.dto.JuegoMesaResponseDTO;
import co.edu.unbosque.services.implementations.JuegoMesaService;
import co.edu.unbosque.services.interfaces.IJuegoMesaService;
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

@Path("/juegoMesa")
@Consumes (MediaType.APPLICATION_JSON)
@Produces (MediaType.APPLICATION_JSON)
public class JuegoMesaController implements IJuegoMesaController {
    private final IJuegoMesaService juegoMesaService=new JuegoMesaService();

    @Override
    @POST 
    public Response guardarJuego(JuegoMesaRequestDTO request) {
        JuegoMesaResponseDTO juegoM=juegoMesaService.addJuegoMesa(request);
        if(juegoM==null){
            return Response.status(Response.Status.BAD_REQUEST)
            .entity("No se pudo registrar el juego de mesa")
            .build();
        }
        return Response.status(Response.Status.CREATED)
        .entity(juegoM)
        .build();
    }

  @Override
  @GET 
  @Path("/buscar") 
  public Response buscarPorNombre( @QueryParam("nombre") String nombre) {
     List<JuegoMesaResponseDTO>juegos=juegoMesaService.getJuegoMesa(nombre);
        return Response.ok(juegos).build();
  }  
  
  @Override
  @PUT 
  @Path("/{numSerie}")
  public Response actualizarJuegoMesa(@PathParam("numSerie") String numSerie, JuegoMesaRequestDTO request) {
      JuegoMesaResponseDTO juego=juegoMesaService.updateJuegoMesa(numSerie, request);
      if(juego==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se ha encontrado el juego que desea actualizar")
        .build();
      }
      return Response.ok(juego).build();
  }

  
  @Override
  @DELETE 
  @Path("/{numSerie}")
  public Response borrarJuegoMesa(@PathParam("numSerie") String numSerie) {
    JuegoMesaResponseDTO juego=juegoMesaService.deleteJuegoMesa(numSerie);
    if(juego==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se a encontrado el juego")
        .build();
    }
      return Response.ok(juego).build();
  }


  @Override
  @GET 
  @Path("/buscar_numSerie")
  public Response buscarPorNumSerie(@PathParam("numSerie") String numSerie) {
     JuegoMesaResponseDTO juego=juegoMesaService.getJuegosPorNumSerie(numSerie);
     if(juego==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se ha encontrado el juego")
        .build();
     }
      return Response.ok(juego).build();
  }

  

}
