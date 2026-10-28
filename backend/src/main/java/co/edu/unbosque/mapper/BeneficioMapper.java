package mapper;

import dto.BeneficioRequest;
import dto.BeneficioResponse;
import model.Beneficio;

import java.util.ArrayList;
import java.util.List;

public final class BeneficioMapper {

    private BeneficioMapper() {
    }

    public static Beneficio toEntity(BeneficioRequest request) {
        if (request == null) {
            return null;
        }
        Beneficio entity = new Beneficio();
        entity.setIdMembresia(request.getIdMembresia());
        entity.setIdBeneficio(request.getIdBeneficio());
        entity.setBeneficio(request.getBeneficio());
        return entity;
    }

    public static BeneficioResponse toResponse(Beneficio entity) {
        if (entity == null) {
            return null;
        }
        return new BeneficioResponse(
                entity.getIdMembresia(),
                entity.getIdBeneficio(),
                entity.getBeneficio()
        );
    }

    public static List<BeneficioResponse> toResponseList(List<Beneficio> entities) {
        List<BeneficioResponse> responses = new ArrayList<>();
        if (entities == null) {
            return responses;
        }
        for (Beneficio entity : entities) {
            responses.add(toResponse(entity));
        }
        return responses;
    }
}