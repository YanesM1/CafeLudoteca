package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.ICategoriaController;
import co.edu.unbosque.model.dto.CategoriaRequestDTO;
import co.edu.unbosque.model.dto.CategoriaResponseDTO;
import co.edu.unbosque.services.implementations.CategoriaService;
import co.edu.unbosque.services.interfaces.ICategoriaService;
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

@Path ("/categoria")
@Consumes (MediaType.APPLICATION_JSON)
@Produces (MediaType.APPLICATION_JSON)
public class CategoriaController implements  ICategoriaController{
    private final ICategoriaService categoriaService=new CategoriaService();


    @Override
    @POST 
    public Response guardarCategoria(CategoriaRequestDTO request) {
       CategoriaResponseDTO categoriaJ=categoriaService.addCategoria(request);
        if(categoriaJ==null){
            return Response.status(Response.Status.BAD_REQUEST)
            .entity("No se pudo registrar la categoria en la base de datos")
            .build();
        }
        return Response.status(Response.Status.CREATED)
        .entity(categoriaJ)
        .build();
    }

@Override
@GET 
@Path("/buscar/{categoria}") 
public Response buscarPorCategoria(@PathParam ("categoria") String categoria) {
         List<CategoriaResponseDTO>categorias=categoriaService.getCategoria(categoria);
        return Response.ok(categorias).build();
}


@Override
@GET 
@Path("/buscar_num_categoria/{num_categoria}/{num_serie}")
public Response buscarPorNumCategoria(@PathParam ("num_categoria") Integer numCategoria, 
@PathParam ("num_serie") String numSerie) {
     CategoriaResponseDTO categoriaJ=categoriaService.getCategoriaNumSerie(numCategoria, numSerie);
     if(categoriaJ==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se ha encontrado la categoría en la base de datos")
        .build();
     }
      return Response.ok(categoriaJ).build();
}

@Override
@PUT 
@Path("/{num_categoria}/{num_serie}")
public Response actualizarCategoria(@PathParam ("num_categoria") Integer numCategoria, @PathParam("num_serie") String numSerie, CategoriaRequestDTO request) {
    CategoriaResponseDTO categoriaJ=categoriaService.updateCategoria(numCategoria, numSerie, request);
      if(categoriaJ==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("Error, no se ha podido actualizar la categoria")
        .build();
      }
      return Response.ok(categoriaJ).build();
}

@Override
  @DELETE 
  @Path("/borrar/{num_categoria}/{num_serie}")
public Response borrarCategoria(@PathParam ("num_categoria") Integer numCategoria, @PathParam ("num_serie") String numSerie) {
     CategoriaResponseDTO categoriaJ=categoriaService.deleteCategoria(numCategoria, numSerie);
    if(categoriaJ==null){
        return Response.status(Response.Status.NOT_FOUND)
        .entity("No se a encontrado el juego")
        .build();
    }
      return Response.ok(categoriaJ).build();
}

}

