package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.TelefonoMapper;
import co.edu.unbosque.model.dto.TelefonoRequestDTO;
import co.edu.unbosque.model.dto.TelefonoResponseDTO;
import co.edu.unbosque.model.entity.Telefono;
import co.edu.unbosque.repository.implementations.TelefonoRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.ITelefonoRepository;
import co.edu.unbosque.services.interfaces.ITelefonoService;

public class TelefonoService implements ITelefonoService {

    private final ITelefonoRepository telefonoRepository = new TelefonoRepositoryJDBC();
    private final TelefonoMapper telefonoMapper = new TelefonoMapper();

    private boolean validarTelefono(TelefonoRequestDTO request) {

        if (request == null) {
            return false;
        }

        if (request.getDocumentoCliente() == null || request.getDocumentoCliente().isBlank()) {
            return false;
        }
        if (request.getTelefono() == null || request.getTelefono().isBlank()) {
            return false;
        }

        return true;
    }

    @Override
    public TelefonoResponseDTO addTelefono(TelefonoRequestDTO request) {

        if (!validarTelefono(request)) {
            return null;
        }

        Telefono telefono = telefonoMapper.telefonoRequestDTOToTelefono(request);

        if (!telefonoRepository.saveTelefono(telefono)) {
            return null;
        }

        return telefonoMapper.telefonoToResponseDTO(telefono);
    }

    @Override
    public List<TelefonoResponseDTO> getTelefonosPorDocumento(String documentoCliente) {

        List<Telefono> telefonos = telefonoRepository.findByDocumento(documentoCliente);
        List<TelefonoResponseDTO> response = new ArrayList<>();

        if (telefonos == null) {
            return response;
        }

        for (Telefono telefono : telefonos) {
            response.add(telefonoMapper.telefonoToResponseDTO(telefono));
        }

        return response;
    }

    

    @Override
    public TelefonoResponseDTO updateTelefono(String documentoCliente, String telefonoActual,
            TelefonoRequestDTO request) {

        if (!validarTelefono(request)) {
            return null;
        }

        Telefono telefono = telefonoMapper.telefonoRequestDTOToTelefono(request);

        if (!telefonoRepository.updateTelefono(documentoCliente, telefonoActual, telefono)) {
            return null;
        }

        // El numero hace parte de la PK: se relee con el numero nuevo
        Optional<Telefono> actualizado = telefonoRepository
                .findByDocumentoAndNumero(documentoCliente, request.getTelefono());

        if (actualizado.isEmpty()) {
            return null;
        }

        return telefonoMapper.telefonoToResponseDTO(actualizado.get());
    }

    @Override
    public TelefonoResponseDTO deleteTelefono(String documentoCliente, String telefono) {

        Optional<Telefono> telefonoC =
                telefonoRepository.findByDocumentoAndNumero(documentoCliente, telefono);

        if (telefonoC.isEmpty()) {
            return null;
        }

        if (!telefonoRepository.deleteTelefono(documentoCliente, telefono)) {
            return null;
        }

        return telefonoMapper.telefonoToResponseDTO(telefonoC.get());
    }
}
