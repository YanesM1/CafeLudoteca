package mapper;

import dto.MembresiaRequest;
import dto.MembresiaResponse;
import model.Membresia;

import java.util.ArrayList;
import java.util.List;

public final class MembresiaMapper {

    private MembresiaMapper() {
    }

    public static Membresia toEntity(MembresiaRequest request) {
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

    public static MembresiaResponse toResponse(Membresia entity) {
        if (entity == null) {
            return null;
        }
        return new MembresiaResponse(
                entity.getIdMembresia(),
                entity.getPlan(),
                entity.getEstado(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getDocumento(),
                entity.getTarifa()
        );
    }

    public static List<MembresiaResponse> toResponseList(List<Membresia> entities) {
        List<MembresiaResponse> responses = new ArrayList<>();
        if (entities == null) {
            return responses;
        }
        for (Membresia entity : entities) {
            responses.add(toResponse(entity));
        }
        return responses;
    }
}