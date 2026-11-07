package co.edu.unbosque.controller.implementations;

import co.edu.unbosque.config.ErrorResponses;

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
            return ErrorResponses.of(Response.Status.BAD_REQUEST, "No se pudo registrar el juego de mesa");
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
  @Path("/{num_serie}")
  public Response actualizarJuegoMesa(@PathParam("num_serie") String numSerie, JuegoMesaRequestDTO request) {
      JuegoMesaResponseDTO juego=juegoMesaService.updateJuegoMesa(numSerie, request);
      if(juego==null){
        return ErrorResponses.of(Response.Status.NOT_FOUND, "No se ha encontrado el juego que desea actualizar");
      }
      return Response.ok(juego).build();
  }

  
  @Override
  @DELETE 
  @Path("/{num_serie}")
  public Response borrarJuegoMesa(@PathParam("num_serie") String numSerie) {
    JuegoMesaResponseDTO juego=juegoMesaService.deleteJuegoMesa(numSerie);
    if(juego==null){
        return ErrorResponses.of(Response.Status.NOT_FOUND, "No se a encontrado el juego");
    }
      return Response.ok(juego).build();
  }


  @Override
  @GET 
  @Path("/buscar/{num_serie}")
  public Response buscarPorNumSerie(@PathParam("num_serie") String numSerie) {
     JuegoMesaResponseDTO juego=juegoMesaService.getJuegosPorNumSerie(numSerie);
     if(juego==null){
        return ErrorResponses.of(Response.Status.NOT_FOUND, "No se ha encontrado el juego");
     }
      return Response.ok(juego).build();
  }

  

}
