package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.ClienteRequestDTO;
import co.edu.unbosque.model.dto.ClienteResponseDTO;
import co.edu.unbosque.model.entity.Cliente;

public class ClienteMapper {

    public Cliente clienteRequestDTOToCliente(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setDocumento(dto.getDocumento());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setPrimerNombre(dto.getPrimerNombre());
        cliente.setSegundoNombre(dto.getSegundoNombre());
        cliente.setPrimerApellido(dto.getPrimerApellido());
        cliente.setSegundoApellido(dto.getSegundoApellido());
        cliente.setDireccion(dto.getDireccion());

        return cliente;
    }

    public ClienteResponseDTO clienteToResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setDocumento(cliente.getDocumento());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        dto.setPrimerNombre(cliente.getPrimerNombre());
        dto.setSegundoNombre(cliente.getSegundoNombre());
        dto.setPrimerApellido(cliente.getPrimerApellido());
        dto.setSegundoApellido(cliente.getSegundoApellido());
        dto.setDireccion(cliente.getDireccion());

        return dto;
    }
}