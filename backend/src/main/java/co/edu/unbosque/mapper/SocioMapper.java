package mapper;

import dto.SocioRequest;
import dto.SocioResponse;
import model.Socio;

import java.util.ArrayList;
import java.util.List;

public final class SocioMapper {

    private SocioMapper() {
    }

    public static Socio toEntity(SocioRequest request) {
        if (request == null) {
            return null;
        }
        Socio entity = new Socio();
        entity.setDocumento(request.getDocumento());
        entity.setFechaAfiliacion(request.getFechaAfiliacion());
        return entity;
    }

    public static SocioResponse toResponse(Socio entity) {
        if (entity == null) {
            return null;
        }
        return new SocioResponse(
                entity.getDocumento(),
                entity.getFechaAfiliacion()
        );
    }

    public static List<SocioResponse> toResponseList(List<Socio> entities) {
        List<SocioResponse> responses = new ArrayList<>();
        if (entities == null) {
            return responses;
        }
        for (Socio entity : entities) {
            responses.add(toResponse(entity));
        }
        return responses;
    }
}