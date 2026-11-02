package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IExpansionController;
import co.edu.unbosque.model.dto.ExpansionRequestDTO;
import co.edu.unbosque.model.dto.ExpansionResponseDTO;
import co.edu.unbosque.services.implementations.ExpansionService;
import co.edu.unbosque.services.interfaces.IExpansionService;
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


@Path ("/expansion")
@Consumes (MediaType.APPLICATION_JSON)
@Produces (MediaType.APPLICATION_JSON)
public class ExpansionController implements  IExpansionController{
    private final IExpansionService expansionService=new ExpansionService();


@Override
 @POST 
public Response guardarExpansion(ExpansionRequestDTO request) {
     ExpansionResponseDTO expansionJ=expansionService.addExpansion(request);
        if(expansionJ==null){
            return Response.status(Response.Status.BAD_REQUEST)
            .entity("No se pudo registrar el juego de mesa")
            .build();
        }
        return Response.status(Response.Status.CREATED)
        .entity(expansionJ)
        .build();
}


@GET
@Path("/buscar/{nombre}")
public Response buscarPorNombre(@PathParam("nombre") String nombre) {
    List<ExpansionResponseDTO> expansiones = expansionService.getExpansion(nombre);
    return Response.ok(expansiones).build();
}

    

@Override
@GET 
@Path("/buscar_expansion/{id_expansion}")
public Response buscarPorIdExpansion(@PathParam("id_expansion") Integer idExpansion) {
    ExpansionResponseDTO expansionJ=expansionService.getExpansionPorIdExpansion(idExpansion);
     if(expansionJ==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se ha encontrado el juego")
        .build();
     }
      return Response.ok(expansionJ).build();
}

@Override
@PUT 
@Path("/actualizar/{id_expansion}")
public Response actualizarExpansion(@PathParam("id_expansion") Integer idExpansion, 
ExpansionRequestDTO request) {
    ExpansionResponseDTO expansionJ=expansionService.updateExpansion(idExpansion, request);
      if(expansionJ==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se ha encontrado el juego que desea actualizar")
        .build();
      }
      return Response.ok(expansionJ).build();
}


@Override
 @DELETE 
@Path("/borrar/{id_expansion}")
public Response borrarJuegoMesa(@PathParam("id_expansion") Integer idExpansion) {
     ExpansionResponseDTO expansionJ=expansionService.deleteExpansion(idExpansion);
    if(expansionJ==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se a encontrado el juego")
        .build();
    }
      return Response.ok(expansionJ).build();
}
}
