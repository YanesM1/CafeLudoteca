package co.edu.unbosque.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public SocioResponseDTO addSocio(SocioRequestDTO request) {
        if (request == null) {
            return null;
        }

        Socio socio = socioMapper.socioRequestDTOToSocio(request);

        if (!socioRepository.saveSocio(socio)) {
            return null;
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
}