
package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
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

    // VALIDAR DATOS DE LA RESERVA
    private boolean validarReserva(
            ReservaRequestDTO request,
            boolean validarId) {

        if (request == null) {
            return false;
        }

        if (validarId &&
                (request.getIdReserva() == null ||
                 request.getIdReserva().isBlank())) {
            return false;
        }

        if (request.getFecha() == null) {
            return false;
        }

        if (request.getHorario() == null ||
                request.getHorario().isBlank()) {
            return false;
        }

        if (request.getDocumento() == null ||
                request.getDocumento().isBlank()) {
            return false;
        }

        if (request.getIdMesa() == null ||
                request.getIdMesa().isBlank()) {
            return false;
        }

        return true;
    }

    // REGISTRAR RESERVA
    @Override
    public ReservaResponseDTO addReserva(ReservaRequestDTO request) {

        if (!validarReserva(request, true)) {
            return null;
        }

        Reserva reserva = ReservaMapper.toEntity(request);

        if (!reservaRepository.saveReserva(reserva)) {
            return null;
        }

        return ReservaMapper.toResponse(reserva);
    }

    // CONSULTAR RESERVAS POR DOCUMENTO DEL CLIENTE
    @Override
    public List<ReservaResponseDTO> getReserva(String documento) {

        if (documento == null || documento.isBlank()) {
            return new ArrayList<>();
        }

        List<Reserva> reservas =
                reservaRepository.findByDocumento(documento.trim());

        return ReservaMapper.toResponseList(reservas);
    }

    // CONSULTAR RESERVA POR IDENTIFICADOR
    @Override
    public ReservaResponseDTO getReservasPorIdReserva(String idReserva) {

        if (idReserva == null || idReserva.isBlank()) {
            return null;
        }

        Optional<Reserva> reserva =
                reservaRepository.findByIdReserva(idReserva.trim());

        return reserva
                .map(ReservaMapper::toResponse)
                .orElse(null);
    }

    // ACTUALIZAR RESERVA
    @Override
    public ReservaResponseDTO updateReserva(
            String idReserva,
            ReservaRequestDTO request) {

        if (idReserva == null || idReserva.isBlank()) {
            return null;
        }

        if (!validarReserva(request, false)) {
            return null;
        }

        // Evitar modificar accidentalmente la clave primaria
        if (request.getIdReserva() != null &&
                !request.getIdReserva().isBlank() &&
                !idReserva.equals(request.getIdReserva())) {
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

    // ELIMINAR RESERVA
    @Override
    public ReservaResponseDTO deleteReserva(String idReserva) {

        if (idReserva == null || idReserva.isBlank()) {
            return null;
        }

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
