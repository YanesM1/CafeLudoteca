
package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.EmpleadoRequestDTO;
import co.edu.unbosque.model.dto.EmpleadoResponseDTO;

public interface IEmpleadoService {
    EmpleadoResponseDTO addEmpleado(EmpleadoRequestDTO request);
    List<EmpleadoResponseDTO> getEmpleado(String nombre);
    List<EmpleadoResponseDTO> getTodos();
    EmpleadoResponseDTO getEmpleadoPorDocumento(String documentoEmp);
    EmpleadoResponseDTO updateEmpleado(String documentoEmp, EmpleadoRequestDTO request);
    EmpleadoResponseDTO deleteEmpleado(String documentoEmp);

}
