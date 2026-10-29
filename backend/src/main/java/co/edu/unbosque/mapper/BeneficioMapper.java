package co.edu.unbosque.mapper;

import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;
import co.edu.unbosque.model.entity.Beneficio;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BeneficioMapper {

    public static Beneficio toEntity(BeneficioRequestDTO request) {
        if (request == null) {
            return null;
        }

        Beneficio entity = new Beneficio();
        entity.setIdBeneficio(request.getIdBeneficio());
        entity.setBeneficio(request.getBeneficio());
        entity.setIdMembresia(request.getIdMembresia());

        return entity;
    }

    public static BeneficioResponseDTO toResponse(Beneficio entity) {
        if (entity == null) {
            return null;
        }

        return new BeneficioResponseDTO(
                entity.getIdBeneficio(),
                entity.getBeneficio(),
                entity.getIdMembresia()
        );
    }

    public static List<BeneficioResponseDTO> toResponseList(List<Beneficio> entities) {
        List<BeneficioResponseDTO> responses = new ArrayList<>();

        if (entities == null) {
            return responses;
        }

        for (Beneficio entity : entities) {
            if (entity != null) {
                responses.add(toResponse(entity));
            }
        }

        return responses;
    }
}