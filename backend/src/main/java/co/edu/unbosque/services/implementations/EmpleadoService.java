
package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.EmpleadoMapper;
import co.edu.unbosque.model.dto.EmpleadoRequestDTO;
import co.edu.unbosque.model.dto.EmpleadoResponseDTO;
import co.edu.unbosque.model.entity.Empleado;
import co.edu.unbosque.repository.implementations.EmpleadoRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IEmpleadoRepository;
import co.edu.unbosque.services.interfaces.IEmpleadoService;

public class EmpleadoService implements IEmpleadoService {

    private final IEmpleadoRepository empleadoRepository =
            new EmpleadoRepositoryJDBC();

    @Override
    public EmpleadoResponseDTO addEmpleado(EmpleadoRequestDTO request) {
        if (request == null) {
            return null;
        }

        Empleado empleado = EmpleadoMapper.toEntity(request);

        if (!empleadoRepository.saveEmpleado(empleado)) {
            return null;
        }

        return EmpleadoMapper.toResponse(empleado);
    }

    @Override
    public List<EmpleadoResponseDTO> getEmpleado(String nombre) {
        List<Empleado> empleados =
                empleadoRepository.findByNombre(nombre);

        return EmpleadoMapper.toResponseList(empleados);
    }

    @Override
    public EmpleadoResponseDTO getEmpleadoPorDocumento(String documento) {
        Optional<Empleado> empleado =
                empleadoRepository.findByDocumento(documento);

        return empleado
                .map(EmpleadoMapper::toResponse)
                .orElse(null);
    }

    @Override
    public EmpleadoResponseDTO updateEmpleado(
            String documento,
            EmpleadoRequestDTO request) {

        if (documento == null || request == null) {
            return null;
        }

        Empleado empleado = EmpleadoMapper.toEntity(request);

        if (!empleadoRepository.updateEmpleado(documento, empleado)) {
            return null;
        }

        return empleadoRepository.findByDocumento(documento)
                .map(EmpleadoMapper::toResponse)
                .orElse(null);
    }

    @Override
    public EmpleadoResponseDTO deleteEmpleado(String documento) {
        Optional<Empleado> empleado =
                empleadoRepository.findByDocumento(documento);

        if (empleado.isEmpty()) {
            return null;
        }

        if (!empleadoRepository.deleteEmpleado(documento)) {
            return null;
        }

        return EmpleadoMapper.toResponse(empleado.get());
    }
}
