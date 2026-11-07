package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.repository.implementations.ClienteRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IClienteRepository;
import co.edu.unbosque.mapper.ClienteFrecuenteMapper;
import co.edu.unbosque.model.dto.ClienteFrecuenteRequestDTO;
import co.edu.unbosque.model.dto.ClienteFrecuenteResponseDTO;
import co.edu.unbosque.model.entity.ClienteFrecuente;
import co.edu.unbosque.repository.implementations.ClienteFrecuenteRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IClienteFrecuenteRepository;
import co.edu.unbosque.services.interfaces.IClienteFrecuenteService;

public class ClienteFrecuenteService implements IClienteFrecuenteService {

    private final IClienteFrecuenteRepository clienteFrecuenteRepository =
            new ClienteFrecuenteRepositoryJDBC();

    private final ClienteFrecuenteMapper clienteFrecuenteMapper =
            new ClienteFrecuenteMapper();

    private final IClienteRepository clientes = new ClienteRepositoryJDBC();

    private void validarDatos(ClienteFrecuenteRequestDTO request) {
        if (request.getPuntosAcumulados() < 0) {
            throw BusinessException.badRequest("Los puntos no pueden ser negativos.");
        }
        if (request.getNivelFidelidad() == null || request.getNivelFidelidad().isBlank()) {
            throw BusinessException.badRequest("Selecciona el nivel de fidelidad.");
        }
        request.setNivelFidelidad(request.getNivelFidelidad().trim());
    }

    @Override
    public ClienteFrecuenteResponseDTO addClienteFrecuente(
            ClienteFrecuenteRequestDTO request) {

        if (request == null || request.getDocumentoCliente() == null
                || request.getDocumentoCliente().isBlank()) {
            throw BusinessException.badRequest("Selecciona el cliente.");
        }
        validarDatos(request);
        String documento = request.getDocumentoCliente().trim();
        request.setDocumentoCliente(documento);
        if (clientes.findByDocumento(documento).isEmpty()) {
            throw BusinessException.notFound("El cliente " + documento + " no está registrado.");
        }
        if (clienteFrecuenteRepository.findByDocumento(documento).isPresent()) {
            throw BusinessException.conflict("Ese cliente ya está registrado como cliente frecuente.");
        }

        ClienteFrecuente clienteFrecuente =
                clienteFrecuenteMapper.clienteFrecuenteRequestDTOToClienteFrecuente(request);

        if (!clienteFrecuenteRepository.saveClienteFrecuente(clienteFrecuente)) {
            throw BusinessException.badRequest("No fue posible registrar el cliente frecuente.");
        }

        return clienteFrecuenteMapper.clienteFrecuenteToResponseDTO(clienteFrecuente);
    }

    @Override
    public List<ClienteFrecuenteResponseDTO> getClientesFrecuentesPorNivel(
            String nivelFidelidad) {

        List<ClienteFrecuente> clientesFrecuentes =
                clienteFrecuenteRepository.findByNivelFidelidad(nivelFidelidad);

        return clientesFrecuentes.stream()
                .map(clienteFrecuenteMapper::clienteFrecuenteToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteFrecuenteResponseDTO getClienteFrecuentePorDocumento(
            String documento) {

        Optional<ClienteFrecuente> clienteFrecuente =
                clienteFrecuenteRepository.findByDocumento(documento);

        return clienteFrecuente
                .map(clienteFrecuenteMapper::clienteFrecuenteToResponseDTO)
                .orElse(null);
    }

    @Override
    public ClienteFrecuenteResponseDTO updateClienteFrecuente(
            String documento,
            ClienteFrecuenteRequestDTO request) {

        if (documento == null || request == null) {
            return null;
        }
        validarDatos(request);
        request.setDocumentoCliente(documento);

        ClienteFrecuente clienteFrecuente =
                clienteFrecuenteMapper.clienteFrecuenteRequestDTOToClienteFrecuente(request);

        if (!clienteFrecuenteRepository.updateClienteFrecuente(
                documento, clienteFrecuente)) {
            return null;
        }

        return clienteFrecuenteRepository.findByDocumento(documento)
                .map(clienteFrecuenteMapper::clienteFrecuenteToResponseDTO)
                .orElse(null);
    }

    @Override
    public ClienteFrecuenteResponseDTO deleteClienteFrecuente(String documento) {
        Optional<ClienteFrecuente> clienteFrecuente =
                clienteFrecuenteRepository.findByDocumento(documento);

        if (clienteFrecuente.isEmpty()) {
            return null;
        }

        if (!clienteFrecuenteRepository.deleteClienteFrecuente(documento)) {
            return null;
        }

        return clienteFrecuenteMapper.clienteFrecuenteToResponseDTO(clienteFrecuente.get());
    }

    @Override
    public List<ClienteFrecuenteResponseDTO> getTodos() {
        return clienteFrecuenteRepository.findAll().stream()
                .map(clienteFrecuenteMapper::clienteFrecuenteToResponseDTO)
                .collect(Collectors.toList());
    }
}
