package co.edu.unbosque.mapper;
import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.model.dto.ClienteFrecuenteRequestDTO;
import co.edu.unbosque.model.dto.ClienteFrecuenteResponseDTO;
import co.edu.unbosque.model.entity.ClienteFrecuente;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ClienteFrecuenteMapper {
     public static ClienteFrecuente toEntity(ClienteFrecuenteRequestDTO request) {
        if (request == null) {
            return null;
        }
        ClienteFrecuente entity = new ClienteFrecuente();
        entity.setDocumento(request.getDocumento());
        entity.setPuntosAcumulados(request.getPuntosAcumulados());
        entity.setNivelFidelidad(request.getNivelFidelidad());
        return entity;
    }

    public static ClienteFrecuenteResponseDTO toResponse(ClienteFrecuente entity) {
        if (entity == null) {
            return null;
        }
        return new ClienteFrecuenteResponseDTO(
                entity.getDocumento(),
                entity.getPuntosAcumulados(),
                entity.getNivelFidelidad()
        );
    }

    public static List<ClienteFrecuenteResponseDTO> toResponseList(List<ClienteFrecuente> entities) {
        List<ClienteFrecuenteResponseDTO> responses = new ArrayList<>();
        if (entities == null) {
            return responses;
        }
        for (ClienteFrecuente entity : entities) {
            responses.add(toResponse(entity));
        }
        return responses;
    }

}
