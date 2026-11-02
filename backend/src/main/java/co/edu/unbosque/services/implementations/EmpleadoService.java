
package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
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

    // Validar los datos del empleado
    private boolean validarEmpleado(
            EmpleadoRequestDTO request,
            boolean validarDocumento) {

        if (request == null) {
            return false;
        }

        if (validarDocumento &&
                (request.getDocumento() == null ||
                 request.getDocumento().isBlank())) {
            return false;
        }

        if (request.getNombre() == null ||
                request.getNombre().isBlank()) {
            return false;
        }

        if (request.getApellido() == null ||
                request.getApellido().isBlank()) {
            return false;
        }

        if (request.getCorreo() == null ||
                request.getCorreo().isBlank()) {
            return false;
        }

        if (request.getCargo() == null ||
                request.getCargo().isBlank()) {
            return false;
        }

        return true;
    }

    // REGISTRAR EMPLEADO
    @Override
    public EmpleadoResponseDTO addEmpleado(EmpleadoRequestDTO request) {

        if (!validarEmpleado(request, true)) {
            return null;
        }

        Empleado empleado = EmpleadoMapper.toEntity(request);

        if (!empleadoRepository.saveEmpleado(empleado)) {
            return null;
        }

        return EmpleadoMapper.toResponse(empleado);
    }

    // CONSULTAR EMPLEADOS POR NOMBRE
    @Override
    public List<EmpleadoResponseDTO> getEmpleado(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            return new ArrayList<>();
        }

        List<Empleado> empleados =
                empleadoRepository.findByNombre(nombre.trim());

        return EmpleadoMapper.toResponseList(empleados);
    }

    // CONSULTAR EMPLEADO POR DOCUMENTO
    @Override
    public EmpleadoResponseDTO getEmpleadoPorDocumento(String documento) {

        if (documento == null || documento.isBlank()) {
            return null;
        }

        Optional<Empleado> empleado =
                empleadoRepository.findByDocumento(documento.trim());

        return empleado
                .map(EmpleadoMapper::toResponse)
                .orElse(null);
    }

    // ACTUALIZAR EMPLEADO
    @Override
    public EmpleadoResponseDTO updateEmpleado(
            String documento,
            EmpleadoRequestDTO request) {

        if (documento == null || documento.isBlank()) {
            return null;
        }

        if (!validarEmpleado(request, false)) {
            return null;
        }

        // El documento de la ruta identifica el registro.
        // Si el cuerpo incluye otro documento, se rechaza.
        if (request.getDocumento() != null &&
                !request.getDocumento().isBlank() &&
                !documento.equals(request.getDocumento())) {
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

    // ELIMINAR EMPLEADO
    @Override
    public EmpleadoResponseDTO deleteEmpleado(String documento) {

        if (documento == null || documento.isBlank()) {
            return null;
        }

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
