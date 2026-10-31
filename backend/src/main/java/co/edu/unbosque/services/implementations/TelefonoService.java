package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import co.edu.unbosque.mapper.TelefonoMapper;
import co.edu.unbosque.model.dto.TelefonoRequestDTO;
import co.edu.unbosque.model.dto.TelefonoResponseDTO;
import co.edu.unbosque.model.entity.Telefono;
import co.edu.unbosque.repository.implementations.TelefonoRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.ITelefonoRepository;
import co.edu.unbosque.services.interfaces.ITelefonoService;

public class TelefonoService implements ITelefonoService {

    private final ITelefonoRepository telefonoRepository =
            new TelefonoRepositoryJDBC();

    private final TelefonoMapper telefonoMapper = new TelefonoMapper();

    @Override
    public TelefonoResponseDTO addTelefono(TelefonoRequestDTO request) {
        if (request == null) {
            return null;
        }

        Telefono telefono = telefonoMapper.telefonoRequestDTOToTelefono(request);

        if (!telefonoRepository.saveTelefono(telefono)) {
            return null;
        }

        return telefonoMapper.telefonoToResponseDTO(telefono);
    }

    @Override
    public List<TelefonoResponseDTO> getTelefonosPorDocumento(String documento) {
        List<Telefono> telefonos =
                telefonoRepository.findByDocumento(documento);

        return telefonos.stream()
                .map(telefonoMapper::telefonoToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TelefonoResponseDTO getTelefonoPorNumero(
            String documento,
            String numero) {

        Optional<Telefono> telefono =
                telefonoRepository.findByDocumentoAndNumero(documento, numero);

        return telefono
                .map(telefonoMapper::telefonoToResponseDTO)
                .orElse(null);
    }

    @Override
    public TelefonoResponseDTO updateTelefono(
            String documento,
            String numeroActual,
            TelefonoRequestDTO request) {

        if (documento == null || numeroActual == null || request == null) {
            return null;
        }

        Telefono telefono = telefonoMapper.telefonoRequestDTOToTelefono(request);

        if (!telefonoRepository.updateTelefono(documento, numeroActual, telefono)) {
            return null;
        }

        // El numero hace parte de la PK: se relee con el numero nuevo
        return telefonoRepository
                .findByDocumentoAndNumero(documento, request.getNumero())
                .map(telefonoMapper::telefonoToResponseDTO)
                .orElse(null);
    }

    @Override
    public TelefonoResponseDTO deleteTelefono(String documento, String numero) {
        Optional<Telefono> telefono =
                telefonoRepository.findByDocumentoAndNumero(documento, numero);

        if (telefono.isEmpty()) {
            return null;
        }

        if (!telefonoRepository.deleteTelefono(documento, numero)) {
            return null;
        }

        return telefonoMapper.telefonoToResponseDTO(telefono.get());
    }
}