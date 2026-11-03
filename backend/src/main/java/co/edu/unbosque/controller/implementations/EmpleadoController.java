 package co.edu.unbosque.controller.implementations;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.unbosque.controller.interfaces.IEmpleadoController;
import co.edu.unbosque.exceptions.PersistenceException;
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

@Path("/empleado")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmpleadoController implements IEmpleadoController {

    private static final Logger LOGGER =
            Logger.getLogger(EmpleadoController.class.getName());

    private final IEmpleadoService empleadoService =new EmpleadoService();

    private boolean validarEmpleado(EmpleadoRequestDTO request, boolean validarDocumento) {

        if (request == null) {
            return false;
        }

        if (validarDocumento &&
                (request.getDocumentoEmp() == null || request.getDocumentoEmp().isBlank())) {
            return false;
        }

        return request.getNombre() != null
                && !request.getNombre().isBlank()
                && request.getApellido() != null
                && !request.getApellido().isBlank()
                && request.getCorreo() != null
                && !request.getCorreo().isBlank()
                && request.getCargo() != null
                && !request.getCargo().isBlank();
    }

    
    private Response respuestaError(
            Response.Status estado,
            String mensaje) {

        return Response.status(estado)
                .entity(Map.of("mensaje", mensaje))
                .build();
    }


    private Response errorPersistencia(PersistenceException e) {

        LOGGER.log(Level.SEVERE,
                "Error de persistencia en el módulo de empleados", e);

        return respuestaError(
                Response.Status.INTERNAL_SERVER_ERROR,
                "Ocurrió un error al acceder a la base de datos.");
    }


    @Override
    @POST
    public Response crearEmpleado(EmpleadoRequestDTO request) {

        if (!validarEmpleado(request, true)) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Los datos del empleado son incompletos o inválidos.");
        }

        try {
            EmpleadoResponseDTO empleado =
                    empleadoService.addEmpleado(request);

            if (empleado == null) {
                return respuestaError(
                        Response.Status.INTERNAL_SERVER_ERROR,
                        "No fue posible registrar el empleado.");
            }

            return Response.status(Response.Status.CREATED)
                    .entity(empleado)
                    .build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }


    @GET
    @Path("/listar")
    public Response listarEmpleados() {
        try {
            return Response.ok(empleadoService.getTodos()).build();
        } catch(PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    @Override
    @GET
    @Path("/buscar")
    public Response buscarByNombre(
            @QueryParam("nombre") String nombre) {

        if (nombre == null || nombre.isBlank()) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Debes indicar el nombre que deseas buscar.");
        }

        try {
            List<EmpleadoResponseDTO> empleados =
                    empleadoService.getEmpleado(nombre.trim());

            return Response.ok(empleados).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

 
    @Override
    @GET
    @Path("/{documento_emp}")
    public Response buscarByDocumento(@PathParam("documento_emp") String documentoEmp) {

        if (documentoEmp == null || documentoEmp.isBlank()) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Debes proporcionar el documento.");
        }

        try {
            EmpleadoResponseDTO empleado =
                    empleadoService.getEmpleadoPorDocumento(documentoEmp);

            if (empleado == null) {
                return respuestaError(
                        Response.Status.NOT_FOUND,
                        "No se encontró el empleado.");
            }

            return Response.ok(empleado).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

    @Override
    @PUT
    @Path("/{documento_emp}")
    public Response actualizarEmpleado(
            @PathParam("documento_emp") String documentoEmp,
            EmpleadoRequestDTO request) {

        if (documentoEmp == null || documentoEmp.isBlank()
                || !validarEmpleado(request, false)) {

            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Los datos para actualizar son inválidos.");
        }

        if (request.getDocumentoEmp() != null
                && !request.getDocumentoEmp().isBlank()
                && !documentoEmp.equals(request.getDocumentoEmp())) {

            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "No se permite modificar el documento.");
        }

        try {
            EmpleadoResponseDTO empleado =
                    empleadoService.updateEmpleado(documentoEmp, request);

            if (empleado == null) {
                return respuestaError(
                        Response.Status.NOT_FOUND,
                        "No se encontró el empleado para actualizar.");
            }

            return Response.ok(empleado).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }

   
    @Override
    @DELETE
    @Path("/{documento_emp}")
    public Response eliminarEmpleado(
            @PathParam("documento_emp") String documentoEmp) {

        if (documentoEmp == null || documentoEmp.isBlank()) {
            return respuestaError(
                    Response.Status.BAD_REQUEST,
                    "Debes proporcionar el documento.");
        }

        try {
            EmpleadoResponseDTO empleado =
                    empleadoService.deleteEmpleado(documentoEmp);

            if (empleado == null) {
                return respuestaError(
                        Response.Status.NOT_FOUND,
                        "No se encontró el empleado.");
            }

            return Response.ok(empleado).build();

        } catch (PersistenceException e) {
            return errorPersistencia(e);
        }
    }
}
