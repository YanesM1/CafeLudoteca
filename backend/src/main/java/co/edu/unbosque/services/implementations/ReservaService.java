package co.edu.unbosque.services.implementations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.mapper.ReservaMapper;
import co.edu.unbosque.model.dto.ReservaRequestDTO;
import co.edu.unbosque.model.dto.ReservaResponseDTO;
import co.edu.unbosque.model.entity.Reserva;
import co.edu.unbosque.repository.implementations.ClienteRepositoryJDBC;
import co.edu.unbosque.repository.implementations.JuegoMesaRepositoryJDBC;
import co.edu.unbosque.repository.implementations.MesaRepositoryJDBC;
import co.edu.unbosque.repository.implementations.ReservaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IClienteRepository;
import co.edu.unbosque.repository.interfaces.IJuegoMesaRepository;
import co.edu.unbosque.repository.interfaces.IMesaRepository;
import co.edu.unbosque.repository.interfaces.IReservaRepository;
import co.edu.unbosque.services.Validaciones;
import co.edu.unbosque.services.interfaces.IReservaService;

public class ReservaService implements IReservaService {

    private final IReservaRepository reservaRepository = new ReservaRepositoryJDBC();
    private final IClienteRepository clienteRepository = new ClienteRepositoryJDBC();
    private final IMesaRepository mesaRepository = new MesaRepositoryJDBC();
    private final IJuegoMesaRepository juegoMesaRepository = new JuegoMesaRepositoryJDBC();

    // VALIDAR DATOS DE LA RESERVA (RN-04, RN-05, RF-11)
    private void validarReserva(ReservaRequestDTO request) {
        Validaciones.requerido(request, "Debes enviar los datos de la reserva.");
        Validaciones.requerido(request.getFecha(), "La fecha de la reserva es obligatoria.");
        if (request.getFecha().isBefore(LocalDate.now())) {
            throw BusinessException.badRequest("No se pueden hacer reservas en fechas pasadas.");
        }

        Validaciones.texto(request.getHorario(), "horario");
        String horario = request.getHorario().trim();
        if (!horario.matches("([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?")) {
            throw BusinessException.badRequest("El horario debe tener el formato HH:mm.");
        }
        request.setHorario(horario);
        if (request.getFecha().isEqual(LocalDate.now())
                && java.time.LocalTime.parse(horario.length() == 5 ? horario : horario.substring(0, 5))
                        .isBefore(java.time.LocalTime.now())) {
            throw BusinessException.badRequest("Esa hora ya pasó. Elige una hora posterior para hoy.");
        }

        Validaciones.texto(request.getDocumento(), "documento del cliente");
        request.setDocumento(request.getDocumento().trim());
        if (clienteRepository.findByDocumento(request.getDocumento()).isEmpty()) {
            throw BusinessException.badRequest(
                    "No existe un cliente con el documento " + request.getDocumento() + ".");
        }

        Validaciones.numerico(request.getIdMesa(), "mesa");
        request.setIdMesa(request.getIdMesa().trim());
        if (mesaRepository.findByIdMesa(request.getIdMesa()).isEmpty()) {
            throw BusinessException.badRequest("La mesa seleccionada no existe.");
        }

        if (request.getJuegos() == null || request.getJuegos().isEmpty()) {
            throw BusinessException.badRequest("Selecciona al menos un juego para la reserva.");
        }
        List<String> juegos = new ArrayList<>(new LinkedHashSet<>(request.getJuegos()));
        for (String numSerie : juegos) {
            if (numSerie == null || numSerie.isBlank()
                    || juegoMesaRepository.findByNumSerie(numSerie.trim()).isEmpty()) {
                throw BusinessException.badRequest("El juego " + numSerie + " no existe en el catálogo.");
            }
        }
        request.setJuegos(juegos);
    }

    // REGISTRAR RESERVA
    @Override
    public ReservaResponseDTO addReserva(ReservaRequestDTO request) {
        validarReserva(request);

        Reserva reserva = ReservaMapper.toEntity(request);

        if (!reservaRepository.saveReserva(reserva, request.getJuegos())) {
            return null;
        }

        return ReservaMapper.toResponse(reserva);
    }

    // LISTAR RESERVAS (todas o de una fecha)
    @Override
    public List<ReservaResponseDTO> getTodas(LocalDate fecha) {
        return ReservaMapper.toResponseList(reservaRepository.findAll(fecha));
    }

    // CONSULTAR RESERVAS POR DOCUMENTO DEL CLIENTE
    @Override
    public List<ReservaResponseDTO> getReserva(String documento) {
        if (documento == null || documento.isBlank()) {
            return new ArrayList<>();
        }
        return ReservaMapper.toResponseList(
                reservaRepository.findByDocumento(documento.trim()));
    }

    // CONSULTAR RESERVA POR IDENTIFICADOR
    @Override
    public ReservaResponseDTO getReservasPorIdReserva(String idReserva) {
        if (idReserva == null || idReserva.isBlank()) {
            return null;
        }
        return reservaRepository.findByIdReserva(idReserva.trim())
                .map(ReservaMapper::toResponse)
                .orElse(null);
    }

    // ACTUALIZAR RESERVA
    @Override
    public ReservaResponseDTO updateReserva(String idReserva, ReservaRequestDTO request) {
        if (idReserva == null || idReserva.isBlank()) {
            return null;
        }
        if (reservaRepository.findByIdReserva(idReserva).isEmpty()) {
            return null;
        }
        validarReserva(request);

        Reserva reserva = ReservaMapper.toEntity(request);

        if (!reservaRepository.updateReserva(idReserva, reserva, request.getJuegos())) {
            return null;
        }

        return reservaRepository.findByIdReserva(idReserva)
                .map(ReservaMapper::toResponse)
                .orElse(null);
    }

    // ELIMINAR (CANCELAR) RESERVA
    @Override
    public ReservaResponseDTO deleteReserva(String idReserva) {
        if (idReserva == null || idReserva.isBlank()) {
            return null;
        }

        Optional<Reserva> reserva = reservaRepository.findByIdReserva(idReserva);

        if (reserva.isEmpty()) {
            return null;
        }

        if (!reservaRepository.deleteReserva(idReserva)) {
            return null;
        }

        return ReservaMapper.toResponse(reserva.get());
    }
}
