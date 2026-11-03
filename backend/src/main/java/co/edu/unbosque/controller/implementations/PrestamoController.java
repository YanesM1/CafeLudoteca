package co.edu.unbosque.controller.implementations;

import java.util.List;
import co.edu.unbosque.controller.interfaces.IPrestamoController;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.dto.*;
import co.edu.unbosque.services.implementations.PrestamoServicio;
import co.edu.unbosque.services.interfaces.IPrestamoServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/prestamo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PrestamoController implements IPrestamoController {
    private final IPrestamoServicio service=new PrestamoServicio();

    @POST
    @Override
    public Response guardarPrestamo(PrestamoRequestDTO request) {
        try {
            PrestamoResponseDTO p=service.addPrestamo(request);
            return p==null?Response.status(400).entity("{\"mensaje\":\"Datos inválidos o unidad no disponible\"}").build()
                :Response.status(201).entity(p).build();
        } catch(PersistenceException e) {
            return Response.serverError().entity("{\"mensaje\":\"Error al registrar préstamo\"}").build();
        }
    }

    @GET
    @Path("/buscar")
    @Override
    public Response buscarPorTipoPrestamo(@QueryParam("tipo_prestamo") String tipo) {
        return Response.ok(service.getPrestamo(tipo)).build();
    }

    @GET
    @Path("/activos")
    @Override
    public Response buscarActivos() {
        return Response.ok(service.getActivos()).build();
    }

    @GET
    @Path("/historial")
    @Override
    public Response buscarHistorial() {
        return Response.ok(service.getHistorial()).build();
    }

    @GET
    @Path("/buscar/{id_prestamo}")
    @Override
    public Response buscarPorIdPrestamo(@PathParam("id_prestamo") Integer id) {
        var p=service.getPrestamoByIdPrestamo(id);
        return p==null?Response.status(404).build():Response.ok(p).build();
    }

    @PUT
    @Path("/{id_prestamo}/devolucion")
    @Override
    public Response registrarDevolucion(@PathParam("id_prestamo") Integer id,
            DevolucionPrestamoRequestDTO request) {
        try {
            var p=service.registrarDevolucion(id,request);
            return p==null?Response.status(400).entity("{\"mensaje\":\"No se pudo registrar la devolución\"}").build()
                :Response.ok(p).build();
        } catch(PersistenceException e) {
            return Response.serverError().entity("{\"mensaje\":\"Error al registrar devolución\"}").build();
        }
    }

    @PUT
    @Path("/{id_prestamo}")
    @Override
    public Response actualizarPrestamo(@PathParam("id_prestamo") Integer id,PrestamoRequestDTO request) {
        var p=service.updatePrestamo(id,request);
        return p==null?Response.status(404).build():Response.ok(p).build();
    }

    @DELETE
    @Path("/{id_prestamo}")
    @Override
    public Response borrarPrestamo(@PathParam("id_prestamo") Integer id) {
        var p=service.deletePrestamo(id);
        return p==null?Response.status(409).entity("{\"mensaje\":\"No se puede eliminar un préstamo activo\"}").build()
            :Response.ok(p).build();
    }
}
