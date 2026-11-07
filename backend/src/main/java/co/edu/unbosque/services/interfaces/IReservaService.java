
package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.ReservaRequestDTO;
import co.edu.unbosque.model.dto.ReservaResponseDTO;

public interface IReservaService {

    ReservaResponseDTO addReserva(ReservaRequestDTO request);

    List<ReservaResponseDTO> getReserva(String documento);

    List<ReservaResponseDTO> getTodas(java.time.LocalDate fecha);

    ReservaResponseDTO getReservasPorIdReserva(String idReserva);

    ReservaResponseDTO updateReserva(
            String idReserva,
            ReservaRequestDTO request
    );

    ReservaResponseDTO deleteReserva(String idReserva);

}
