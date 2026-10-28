package mapper;

import dto.ClienteFrecuenteRequest;
import dto.ClienteFrecuenteResponse;
import model.ClienteFrecuente;

import java.util.ArrayList;
import java.util.List;

public final class ClienteFrecuenteMapper {

    private ClienteFrecuenteMapper() {
    }

    public static ClienteFrecuente toEntity(ClienteFrecuenteRequest request) {
        if (request == null) {
            return null;
        }
        ClienteFrecuente entity = new ClienteFrecuente();
        entity.setDocumento(request.getDocumento());
        entity.setPuntosAcumulados(request.getPuntosAcumulados());
        entity.setNivelFidelidad(request.getNivelFidelidad());
        return entity;
    }

    public static ClienteFrecuenteResponse toResponse(ClienteFrecuente entity) {
        if (entity == null) {
            return null;
        }
        return new ClienteFrecuenteResponse(
                entity.getDocumento(),
                entity.getPuntosAcumulados(),
                entity.getNivelFidelidad()
        );
    }

    public static List<ClienteFrecuenteResponse> toResponseList(List<ClienteFrecuente> entities) {
        List<ClienteFrecuenteResponse> responses = new ArrayList<>();
        if (entities == null) {
            return responses;
        }
        for (ClienteFrecuente entity : entities) {
            responses.add(toResponse(entity));
        }
        return responses;
    }
}
