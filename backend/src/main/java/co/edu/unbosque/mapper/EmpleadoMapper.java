
package co.edu.unbosque.mapper;

import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.model.dto.EmpleadoRequestDTO;
import co.edu.unbosque.model.dto.EmpleadoResponseDTO;
import co.edu.unbosque.model.entity.Empleado;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmpleadoMapper {

    // Request -> Entidad
    public static Empleado toEntity(EmpleadoRequestDTO request) {
        if (request == null) {
            return null;
        }

        Empleado empleado = new Empleado();
        empleado.setDocumentoEmp(request.getDocumentoEmp());
        empleado.setCargo(request.getCargo());
        empleado.setNombre(request.getNombre());
        empleado.setApellido(request.getApellido());
        empleado.setCorreo(request.getCorreo());


        return empleado;
    }

    // Entidad -> Response
    public static EmpleadoResponseDTO toResponse(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        return new EmpleadoResponseDTO(
                empleado.getDocumentoEmp(),
                empleado.getCargo(),
                empleado.getNombre(),
                empleado.getApellido(),
                empleado.getCorreo()
                
        );
    }

    // Lista de entidades -> Lista de respuestas
    public static List<EmpleadoResponseDTO> toResponseList(
            List<Empleado> empleados) {

        List<EmpleadoResponseDTO> responses = new ArrayList<>();

        if (empleados == null) {
            return responses;
        }

        for (Empleado empleado : empleados) {
            responses.add(toResponse(empleado));
        }

        return responses;
    }
}
