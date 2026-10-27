package mapper;

import dto.TelefonoRequest;
import dto.TelefonoResponse;
import model.Telefono;

import java.util.ArrayList;
import java.util.List;

public final class TelefonoMapper {

    private TelefonoMapper() {
    }

    public static Telefono toEntity(TelefonoRequest request) {
        if (request == null) {
            return null;
        }
        Telefono entity = new Telefono();
        entity.setDocumento(request.getDocumento());
        entity.setTelefono(request.getTelefono());
        return entity;
    }

    public static TelefonoResponse toResponse(Telefono entity) {
        if (entity == null) {
            return null;
        }
        return new TelefonoResponse(
                entity.getDocumento(),
                entity.getTelefono()
        );
    }

    public static List<TelefonoResponse> toResponseList(List<Telefono> entities) {
        List<TelefonoResponse> responses = new ArrayList<>();
        if (entities == null) {
            return responses;
        }
        for (Telefono entity : entities) {
            responses.add(toResponse(entity));
        }
        return responses;
    }
}