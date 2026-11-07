package co.edu.unbosque.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.repository.implementations.ClienteRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IClienteRepository;
import co.edu.unbosque.mapper.SocioMapper;
import co.edu.unbosque.model.dto.SocioRequestDTO;
import co.edu.unbosque.model.dto.SocioResponseDTO;
import co.edu.unbosque.model.entity.Socio;
import co.edu.unbosque.repository.implementations.SocioRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.ISocioRepository;
import co.edu.unbosque.services.interfaces.ISocioService;

public class SocioService implements ISocioService {

    private final ISocioRepository socioRepository =
            new SocioRepositoryJDBC();

    private final SocioMapper socioMapper = new SocioMapper();

    private final IClienteRepository clientes = new ClienteRepositoryJDBC();

    @Override
    public SocioResponseDTO addSocio(SocioRequestDTO request) {
        co.edu.unbosque.services.Validaciones.requerido(request, "Debes enviar los datos del socio.");
        co.edu.unbosque.services.Validaciones.texto(request.getDocumento(), "documento");
        co.edu.unbosque.services.Validaciones.requerido(
                request.getFechaAfiliacion(), "La fecha de afiliación es obligatoria.");

        String documento = request.getDocumento().trim();
        request.setDocumento(documento);
        if (clientes.findByDocumento(documento).isEmpty()) {
            throw BusinessException.notFound("Primero registra al cliente con documento " + documento + ".");
        }
        if (socioRepository.findByDocumento(documento).isPresent()) {
            throw BusinessException.conflict("Ese cliente ya es socio.");
        }
        if (request.getFechaAfiliacion().isAfter(LocalDate.now())) {
            throw BusinessException.badRequest("La fecha de afiliación no puede ser futura.");
        }

        Socio socio = socioMapper.socioRequestDTOToSocio(request);

        if (!socioRepository.saveSocio(socio)) {
            throw BusinessException.badRequest("No fue posible registrar el socio.");
        }

        return socioMapper.socioToResponseDTO(socio);
    }

    @Override
    public List<SocioResponseDTO> getSociosPorFechaAfiliacion(
            LocalDate fechaAfiliacion) {

        List<Socio> socios =
                socioRepository.findByFechaAfiliacion(fechaAfiliacion);

        return socios.stream()
                .map(socioMapper::socioToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SocioResponseDTO getSocioPorDocumento(String documento) {
        Optional<Socio> socio =
                socioRepository.findByDocumento(documento);

        return socio
                .map(socioMapper::socioToResponseDTO)
                .orElse(null);
    }

    @Override
    public SocioResponseDTO updateSocio(
            String documento,
            SocioRequestDTO request) {

        if (documento == null || request == null) {
            return null;
        }

        Socio socio = socioMapper.socioRequestDTOToSocio(request);

        if (!socioRepository.updateSocio(documento, socio)) {
            return null;
        }

        return socioRepository.findByDocumento(documento)
                .map(socioMapper::socioToResponseDTO)
                .orElse(null);
    }

    @Override
    public SocioResponseDTO deleteSocio(String documento) {
        Optional<Socio> socio =
                socioRepository.findByDocumento(documento);

        if (socio.isEmpty()) {
            return null;
        }

        if (!socioRepository.deleteSocio(documento)) {
            return null;
        }

        return socioMapper.socioToResponseDTO(socio.get());
    }

    @Override
    public List<SocioResponseDTO> getTodos() {
        return socioRepository.findAll().stream()
                .map(socioMapper::socioToResponseDTO)
                .collect(Collectors.toList());
    }
}
