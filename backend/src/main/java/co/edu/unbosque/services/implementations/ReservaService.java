
package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.ReservaMapper;
import co.edu.unbosque.model.dto.ReservaRequestDTO;
import co.edu.unbosque.model.dto.ReservaResponseDTO;
import co.edu.unbosque.model.entity.Reserva;
import co.edu.unbosque.repository.implementations.ReservaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IReservaRepository;
import co.edu.unbosque.services.interfaces.IReservaService;

public class ReservaService implements IReservaService {

    private final IReservaRepository reservaRepository =
            new ReservaRepositoryJDBC();

    @Override
    public ReservaResponseDTO addReserva(ReservaRequestDTO request) {
        if (request == null) {
            return null;
        }

        Reserva reserva = ReservaMapper.toEntity(request);

        if (!reservaRepository.saveReserva(reserva)) {
            return null;
        }

        return ReservaMapper.toResponse(reserva);
    }

    @Override
    public List<ReservaResponseDTO> getReserva(String documento) {
        List<Reserva> reservas =
                reservaRepository.findByDocumento(documento);

        return ReservaMapper.toResponseList(reservas);
    }

    @Override
    public ReservaResponseDTO getReservasPorIdReserva(String idReserva) {
        Optional<Reserva> reserva =
                reservaRepository.findByIdReserva(idReserva);

        return reserva
                .map(ReservaMapper::toResponse)
                .orElse(null);
    }

    @Override
    public ReservaResponseDTO updateReserva(
            String idReserva,
            ReservaRequestDTO request) {

        if (idReserva == null || request == null) {
            return null;
        }

        Reserva reserva = ReservaMapper.toEntity(request);

        if (!reservaRepository.updateReserva(idReserva, reserva)) {
            return null;
        }

        return reservaRepository.findByIdReserva(idReserva)
                .map(ReservaMapper::toResponse)
                .orElse(null);
    }

    @Override
    public ReservaResponseDTO deleteReserva(String idReserva) {
        Optional<Reserva> reserva =
                reservaRepository.findByIdReserva(idReserva);

        if (reserva.isEmpty()) {
            return null;
        }

        if (!reservaRepository.deleteReserva(idReserva)) {
            return null;
        }

        return ReservaMapper.toResponse(reserva.get());
    }
}
