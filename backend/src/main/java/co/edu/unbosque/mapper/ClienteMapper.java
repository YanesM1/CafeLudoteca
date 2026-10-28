package mapper;

import dto.ClienteRequest;
import dto.ClienteResponse;
import model.Cliente;

import java.util.ArrayList;
import java.util.List;

public final class ClienteMapper {

    // Clase de utilidad: no se instancia
    private ClienteMapper() {
    }

    // Request (lo que llega del cliente) -> Entidad (Guarda en DB)
    public static Cliente toEntity(ClienteRequest request) {
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
    public static ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return new ClienteResponse(
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
    public static List<ClienteResponse> toResponseList(List<Cliente> clientes) {
        List<ClienteResponse> responses = new ArrayList<>();
        if (clientes == null) {
            return responses;
        }
        for (Cliente cliente : clientes) {
            responses.add(toResponse(cliente));
        }
        return responses;
    }
}