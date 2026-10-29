
package co.edu.unbosque.controller.interfaces;

import jakarta.ws.rs.core.Response;

import co.edu.unbosque.model.dto.EmpleadoRequestDTO;

public interface IEmpleadoController {

    Response crearEmpleado(EmpleadoRequestDTO request);

    Response buscarEmpleados(String nombre);

    Response obtenerEmpleado(String documento);

    Response actualizarEmpleado(
            String documento,
            EmpleadoRequestDTO request
    );

    Response eliminarEmpleado(String documento);

}
