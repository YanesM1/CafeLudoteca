package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public ClienteFrecuenteResponseDTO addClienteFrecuente(
            ClienteFrecuenteRequestDTO request) {

        if (request == null) {
            return null;
        }

        ClienteFrecuente clienteFrecuente =
                clienteFrecuenteMapper.clienteFrecuenteRequestDTOToClienteFrecuente(request);

        if (!clienteFrecuenteRepository.saveClienteFrecuente(clienteFrecuente)) {
            return null;
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
}