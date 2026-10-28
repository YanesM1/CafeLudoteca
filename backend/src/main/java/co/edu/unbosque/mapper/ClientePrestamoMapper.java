package mapper;

import dto.ClientePrestamoRequest;
import dto.ClientePrestamoResponse;
import model.ClientePrestamo;

import java.util.ArrayList;
import java.util.List;

public final class ClientePrestamoMapper {

    private ClientePrestamoMapper() {
    }

    public static ClientePrestamo toEntity(ClientePrestamoRequest request) {
        if (request == null) {
            return null;
        }
        ClientePrestamo entity = new ClientePrestamo();
        entity.setIdPrestamo(request.getIdPrestamo());
        entity.setDocumento(request.getDocumento());
        return entity;
    }

    public static ClientePrestamoResponse toResponse(ClientePrestamo entity) {
        if (entity == null) {
            return null;
        }
        return new ClientePrestamoResponse(
                entity.getIdPrestamo(),
                entity.getDocumento()
        );
    }

    public static List<ClientePrestamoResponse> toResponseList(List<ClientePrestamo> entities) {
        List<ClientePrestamoResponse> responses = new ArrayList<>();
        if (entities == null) {
            return responses;
        }
        for (ClientePrestamo entity : entities) {
            responses.add(toResponse(entity));
        }
        return responses;
    }
}