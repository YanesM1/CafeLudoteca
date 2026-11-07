package co.edu.unbosque.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.mapper.ClienteMapper;
import co.edu.unbosque.model.dto.ClienteRequestDTO;
import co.edu.unbosque.model.dto.ClienteResponseDTO;
import co.edu.unbosque.model.entity.Cliente;
import co.edu.unbosque.repository.implementations.ClienteRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IClienteRepository;
import co.edu.unbosque.services.Validaciones;
import co.edu.unbosque.services.interfaces.IClienteService;

public class ClienteService implements IClienteService {

    private final IClienteRepository clienteRepository =
            new ClienteRepositoryJDBC();

    private final ClienteMapper clienteMapper = new ClienteMapper();

    // VALIDAR DATOS DEL CLIENTE
    private void validarCliente(ClienteRequestDTO request, boolean validarDocumento) {
        Validaciones.requerido(request, "Debes enviar los datos del cliente.");
        if (validarDocumento) {
            Validaciones.texto(request.getDocumentoCliente(), "documento");
        }
        Validaciones.texto(request.getPrimerNombre(), "primer nombre");
        Validaciones.texto(request.getPrimerApellido(), "primer apellido");
        if (request.getFechaNacimiento() != null
                && request.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw BusinessException.badRequest("La fecha de nacimiento no puede ser futura.");
        }
    }

    @Override
    public ClienteResponseDTO addCliente(ClienteRequestDTO request) {
        validarCliente(request, true);

        Cliente cliente = clienteMapper.clienteRequestDTOToCliente(request);
        cliente.setDocumentoCliente(request.getDocumentoCliente().trim());

        if (clienteRepository.findByDocumento(cliente.getDocumentoCliente()).isPresent()) {
            throw BusinessException.conflict("Ya existe un cliente con ese documento.");
        }

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
    public ClienteResponseDTO getClientePorDocumento(String documentoCliente) {
        Optional<Cliente> cliente =
                clienteRepository.findByDocumento(documentoCliente);

        return cliente
                .map(clienteMapper::clienteToResponseDTO)
                .orElse(null);
    }

    @Override
    public ClienteResponseDTO updateCliente(
            String documentoCliente,
            ClienteRequestDTO request) {

        if (documentoCliente == null || documentoCliente.isBlank()) {
            return null;
        }
        validarCliente(request, false);

        Cliente cliente = clienteMapper.clienteRequestDTOToCliente(request);

        if (!clienteRepository.updateCliente(documentoCliente, cliente)) {
            return null;
        }

        return clienteRepository.findByDocumento(documentoCliente)
                .map(clienteMapper::clienteToResponseDTO)
                .orElse(null);
    }

    @Override
    public ClienteResponseDTO deleteCliente(String documentoCliente) {
        Optional<Cliente> cliente =
                clienteRepository.findByDocumento(documentoCliente);

        if (cliente.isEmpty()) {
            return null;
        }

        if (!clienteRepository.deleteCliente(documentoCliente)) {
            return null;
        }

        return clienteMapper.clienteToResponseDTO(cliente.get());
    }
}
