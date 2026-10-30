
package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.EmpleadoRequestDTO;
import co.edu.unbosque.model.dto.EmpleadoResponseDTO;

public interface IEmpleadoService {

    EmpleadoResponseDTO addEmpleado(EmpleadoRequestDTO request);

    List<EmpleadoResponseDTO> getEmpleado(String nombre);

    EmpleadoResponseDTO getEmpleadoPorDocumento(String documento);

    EmpleadoResponseDTO updateEmpleado(
            String documento,
            EmpleadoRequestDTO request
    );

    EmpleadoResponseDTO deleteEmpleado(String documento);

}
