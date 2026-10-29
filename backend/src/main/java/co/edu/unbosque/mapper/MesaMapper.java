
package co.edu.unbosque.mapper;

import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.model.dto.MesaRequestDTO;
import co.edu.unbosque.model.dto.MesaResponseDTO;
import co.edu.unbosque.model.entity.Mesa;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MesaMapper {

    // Request -> Entidad
    public static Mesa toEntity(MesaRequestDTO request) {
        if (request == null) {
            return null;
        }

        Mesa mesa = new Mesa();
        mesa.setIdMesa(request.getIdMesa());
        mesa.setCapacidad(request.getCapacidad());
        mesa.setTematica(request.getTematica());
        mesa.setUbicacion(request.getUbicacion());

        return mesa;
    }

    // Entidad -> Response
    public static MesaResponseDTO toResponse(Mesa mesa) {
        if (mesa == null) {
            return null;
        }

        return new MesaResponseDTO(
                mesa.getIdMesa(),
                mesa.getCapacidad(),
                mesa.getTematica(),
                mesa.getUbicacion()
        );
    }

    // Lista de entidades -> Lista de respuestas
    public static List<MesaResponseDTO> toResponseList(
            List<Mesa> mesas) {

        List<MesaResponseDTO> responses = new ArrayList<>();

        if (mesas == null) {
            return responses;
        }

        for (Mesa mesa : mesas) {
            responses.add(toResponse(mesa));
        }

        return responses;
    }
}
