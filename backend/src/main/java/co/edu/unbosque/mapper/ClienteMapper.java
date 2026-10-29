package co.edu.unbosque.mapper;
import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.model.dto.ClienteRequestDTO;
import co.edu.unbosque.model.dto.ClienteResponseDTO;
import co.edu.unbosque.model.entity.Cliente;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ClienteMapper {
    // Request (lo que llega del cliente) -> Entidad (Guarda en DB)
    public static Cliente toEntity(ClienteRequestDTO request) {
        if (request == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setDocumento(request.getDocumento());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setPrimerNombre(request.getPrimerNombre());
        cliente.setSegundoNombre(request.getSegundoNombre());
        cliente.setPrimerApellido(request.getPrimerApellido());
        cliente.setSegundoApellido(request.getSegundoApellido());
        cliente.setDireccion(request.getDireccion());
        return cliente;
    }

    // Entidad (lo que lees de la BD) -> Response (lo que devuelves)
    public static ClienteResponseDTO toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return new ClienteResponseDTO(
                cliente.getDocumento(),
                cliente.getFechaNacimiento(),
                cliente.getPrimerNombre(),
                cliente.getSegundoNombre(),
                cliente.getPrimerApellido(),
                cliente.getSegundoApellido(),
                cliente.getDireccion()
        );
    }

    // Lista de entidades -> lista de Response
    public static List<ClienteResponseDTO> toResponseList(List<Cliente> clientes) {
        List<ClienteResponseDTO> responses = new ArrayList<>();
        if (clientes == null) {
            return responses;
        }
        for (Cliente cliente : clientes) {
            responses.add(toResponse(cliente));
        }
        return responses;
    }

}
