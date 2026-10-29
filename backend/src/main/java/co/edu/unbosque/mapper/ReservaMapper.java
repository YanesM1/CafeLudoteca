
package co.edu.unbosque.mapper;

import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.model.dto.ReservaRequestDTO;
import co.edu.unbosque.model.dto.ReservaResponseDTO;
import co.edu.unbosque.model.entity.Reserva;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReservaMapper {

    // Request -> Entidad
    public static Reserva toEntity(ReservaRequestDTO request) {
        if (request == null) {
            return null;
        }

        Reserva reserva = new Reserva();
        reserva.setIdReserva(request.getIdReserva());
        reserva.setFecha(request.getFecha());
        reserva.setHorario(request.getHorario());
        reserva.setDocumento(request.getDocumento());
        reserva.setIdMesa(request.getIdMesa());

        return reserva;
    }

    // Entidad -> Response
    public static ReservaResponseDTO toResponse(Reserva reserva) {
        if (reserva == null) {
            return null;
        }

        return new ReservaResponseDTO(
                reserva.getIdReserva(),
                reserva.getFecha(),
                reserva.getHorario(),
                reserva.getDocumento(),
                reserva.getIdMesa()
        );
    }

    // Lista de entidades -> Lista de respuestas
    public static List<ReservaResponseDTO> toResponseList(
            List<Reserva> reservas) {

        List<ReservaResponseDTO> responses = new ArrayList<>();

        if (reservas == null) {
            return responses;
        }

        for (Reserva reserva : reservas) {
            responses.add(toResponse(reserva));
        }

        return responses;
    }
}
