
package co.edu.unbosque.controller.implementations;

import java.util.List;

import co.edu.unbosque.controller.interfaces.IEmpleadoController;
import co.edu.unbosque.model.dto.EmpleadoRequestDTO;
import co.edu.unbosque.model.dto.EmpleadoResponseDTO;
import co.edu.unbosque.services.implementations.EmpleadoService;
import co.edu.unbosque.services.interfaces.IEmpleadoService;

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

@Path("/empleados")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmpleadoController implements IEmpleadoController {

    private final IEmpleadoService empleadoService =
            new EmpleadoService();

    @Override
    @POST
    public Response crearEmpleado(EmpleadoRequestDTO request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Los datos del empleado son obligatorios.")
                    .build();
        }

        EmpleadoResponseDTO empleado =
                empleadoService.AddEmpleado(request);

        if (empleado == null) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No fue posible registrar el empleado.")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(empleado)
                .build();
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarEmpleados(
            @QueryParam("nombre") String nombre) {

        if (nombre == null || nombre.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Debes indicar el nombre que deseas buscar.")
                    .build();
        }

        List<EmpleadoResponseDTO> empleados =
                empleadoService.getEmpleado(nombre);

        return Response.ok(empleados).build();
    }

    @Override
    @GET
    @Path("/{documento}")
    public Response obtenerEmpleado(
            @PathParam("documento") String documento) {

        EmpleadoResponseDTO empleado =
                empleadoService.getEmpleadoPorDocumento(documento);

        if (empleado == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró el empleado.")
                    .build();
        }

        return Response.ok(empleado).build();
    }

    @Override
    @PUT
    @Path("/{documento}")
    public Response actualizarEmpleado(
            @PathParam("documento") String documento,
            EmpleadoRequestDTO request) {

        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Los datos para actualizar son obligatorios.")
                    .build();
        }

        if (empleadoService.getEmpleadoPorDocumento(documento) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró el empleado.")
                    .build();
        }

        EmpleadoResponseDTO empleado =
                empleadoService.updateEmpleado(documento, request);

        if (empleado == null) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No fue posible actualizar el empleado.")
                    .build();
        }

        return Response.ok(empleado).build();
    }

    @Override
    @DELETE
    @Path("/{documento}")
    public Response eliminarEmpleado(
            @PathParam("documento") String documento) {

        if (empleadoService.getEmpleadoPorDocumento(documento) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró el empleado.")
                    .build();
        }

        EmpleadoResponseDTO empleado =
                empleadoService.deleteEmpleado(documento);

        if (empleado == null) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No fue posible eliminar el empleado.")
                    .build();
        }

        return Response.ok(empleado).build();
    }
}
