package co.edu.unbosque.mapper;
import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import co.edu.unbosque.model.dto.MembresiaResponseDTO;
import co.edu.unbosque.model.entity.Membresia;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MembresiaMapper {
    public static Membresia toEntity(MembresiaRequestDTO request) {
        if (request == null) {
            return null;
        }
        Membresia entity = new Membresia();
        entity.setIdMembresia(request.getIdMembresia());
        entity.setPlan(request.getPlan());
        entity.setEstado(request.getEstado());
        entity.setFechaInicio(request.getFechaInicio());
        entity.setFechaFin(request.getFechaFin());
        entity.setDocumento(request.getDocumento());
        entity.setTarifa(request.getTarifa());
        return entity;
    }

    public static MembresiaResponseDTO toResponse(Membresia entity) {
        if (entity == null) {
            return null;
        }
        return new MembresiaResponseDTO(
                entity.getIdMembresia(),
                entity.getPlan(),
                entity.getEstado(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getDocumento(),
                entity.getTarifa()
        );
    }

    public static List<MembresiaResponseDTO> toResponseList(List<Membresia> entities) {
        List<MembresiaResponseDTO> responses = new ArrayList<>();
        if (entities == null) {
            return responses;
        }
        for (Membresia entity : entities) {
            responses.add(toResponse(entity));
        }
        return responses;
    }

}
