package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import co.edu.unbosque.mapper.ClienteMapper;
import co.edu.unbosque.model.dto.ClienteRequestDTO;
import co.edu.unbosque.model.dto.ClienteResponseDTO;
import co.edu.unbosque.model.entity.Cliente;
import co.edu.unbosque.repository.implementations.ClienteRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IClienteRepository;
import co.edu.unbosque.services.interfaces.IClienteService;

public class ClienteService implements IClienteService {

    private final IClienteRepository clienteRepository =
            new ClienteRepositoryJDBC();

    private final ClienteMapper clienteMapper = new ClienteMapper();

    @Override
    public ClienteResponseDTO addCliente(ClienteRequestDTO request) {
        if (request == null) {
            return null;
        }

        Cliente cliente = clienteMapper.clienteRequestDTOToCliente(request);

        if (!clienteRepository.saveCliente(cliente)) {
            return null;
        }

        return clienteMapper.clienteToResponseDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> getCliente(String nombre) {
        List<Cliente> clientes =
                clienteRepository.findByNombre(nombre);

        return clientes.stream()
                .map(clienteMapper::clienteToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponseDTO getClientePorDocumento(String documento) {
        Optional<Cliente> cliente =
                clienteRepository.findByDocumento(documento);

        return cliente
                .map(clienteMapper::clienteToResponseDTO)
                .orElse(null);
    }

    @Override
    public ClienteResponseDTO updateCliente(
            String documento,
            ClienteRequestDTO request) {

        if (documento == null || request == null) {
            return null;
        }

        Cliente cliente = clienteMapper.clienteRequestDTOToCliente(request);

        if (!clienteRepository.updateCliente(documento, cliente)) {
            return null;
        }

        return clienteRepository.findByDocumento(documento)
                .map(clienteMapper::clienteToResponseDTO)
                .orElse(null);
    }

    @Override
    public ClienteResponseDTO deleteCliente(String documento) {
        Optional<Cliente> cliente =
                clienteRepository.findByDocumento(documento);

        if (cliente.isEmpty()) {
            return null;
        }

        if (!clienteRepository.deleteCliente(documento)) {
            return null;
        }

        return clienteMapper.clienteToResponseDTO(cliente.get());
    }
}