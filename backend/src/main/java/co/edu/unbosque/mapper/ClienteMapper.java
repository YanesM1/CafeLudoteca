package co.edu.unbosque.mapper;
import co.edu.unbosque.model.dto.ClienteRequestDTO;
import co.edu.unbosque.model.dto.ClienteResponseDTO;
import co.edu.unbosque.model.entity.Cliente;

public class ClienteMapper {

    public Cliente clienteRequestDTOToCliente(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setDocumento(dto.getDocumento());
        cliente.setPrimerApellido(dto.getPrimerApellido());
        cliente.setSegundoApellido(dto.getSegundoApellido());
        cliente.setPrimerNombre(dto.getPrimerNombre());
        cliente.setSegundoNombre(dto.getSegundoNombre());
        cliente.setDireccion(dto.getDireccion());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());

        return cliente;
    }

    public ClienteResponseDTO clienteToResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setDocumento(cliente.getDocumento());
        dto.setPrimerApellido(cliente.getPrimerApellido());
        dto.setSegundoApellido(cliente.getSegundoApellido());
        dto.setPrimerNombre(cliente.getPrimerNombre());
        dto.setSegundoNombre(cliente.getSegundoNombre());
        dto.setDireccion(cliente.getDireccion());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());

        return dto;
    }
}