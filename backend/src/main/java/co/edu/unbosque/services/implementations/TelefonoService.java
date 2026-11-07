package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.repository.implementations.ClienteRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IClienteRepository;
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

    private final IClienteRepository clientes = new ClienteRepositoryJDBC();

    /** Solo dígitos, entre 7 y 15 (la columna admite 15 caracteres). */
    private String numeroValido(String telefono) {
        String limpio = telefono == null ? "" : telefono.replaceAll("[\\s-]", "");
        if (!limpio.matches("\\d{7,15}")) {
            throw BusinessException.badRequest("El teléfono debe tener entre 7 y 15 dígitos.");
        }
        return limpio;
    }

    @Override
    public TelefonoResponseDTO addTelefono(TelefonoRequestDTO request) {
        if (request == null || request.getDocumento() == null || request.getDocumento().isBlank()) {
            throw BusinessException.badRequest("Selecciona el cliente.");
        }
        String documento = request.getDocumento().trim();
        request.setDocumento(documento);
        request.setTelefono(numeroValido(request.getTelefono()));
        if (clientes.findByDocumento(documento).isEmpty()) {
            throw BusinessException.notFound("El cliente " + documento + " no está registrado.");
        }
        if (telefonoRepository.findByDocumentoAndNumero(documento, request.getTelefono()).isPresent()) {
            throw BusinessException.conflict("Ese teléfono ya está registrado para el cliente.");
        }

        Telefono telefono = telefonoMapper.telefonoRequestDTOToTelefono(request);

        if (!telefonoRepository.saveTelefono(telefono)) {
            throw BusinessException.badRequest("No fue posible registrar el teléfono.");
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
        request.setDocumento(documento);
        request.setTelefono(numeroValido(request.getTelefono()));

        Telefono telefono = telefonoMapper.telefonoRequestDTOToTelefono(request);

        if (!telefonoRepository.updateTelefono(documento, numeroActual, telefono)) {
            return null;
        }

        // El numero hace parte de la PK: se relee con el numero nuevo
        return telefonoRepository
                .findByDocumentoAndNumero(documento, request.getTelefono())
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