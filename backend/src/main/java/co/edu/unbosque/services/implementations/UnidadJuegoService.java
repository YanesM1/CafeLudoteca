package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.mapper.UnidadJuegoMapper;
import co.edu.unbosque.model.dto.DisponibilidadUnidadJuegoDTO;
import co.edu.unbosque.model.dto.UnidadJuegoRequestDTO;
import co.edu.unbosque.model.dto.UnidadJuegoResponseDTO;
import co.edu.unbosque.model.entity.UnidadJuego;
import co.edu.unbosque.repository.implementations.UnidadJuegoRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IUnidadJuegoRepository;
import co.edu.unbosque.services.Validaciones;
import co.edu.unbosque.services.interfaces.IUnidadJuegoService;

public class UnidadJuegoService implements IUnidadJuegoService {

    /** Estados permitidos para una unidad física (supuesto 4 de la propuesta). */
    public static final String DISPONIBLE = "DISPONIBLE";
    public static final String EN_MESA = "EN_MESA";
    public static final String PRESTADO = "PRESTADO";
    public static final String MANTENIMIENTO = "MANTENIMIENTO";
    public static final String FALTANTES = "FALTANTES";

    private static final Set<String> ESTADOS =
            Set.of(DISPONIBLE, EN_MESA, PRESTADO, MANTENIMIENTO, FALTANTES);

    private final IUnidadJuegoRepository unidadJuegoRepository = new UnidadJuegoRepositoryJDBC();
    private final UnidadJuegoMapper unidadJuegoMapper = new UnidadJuegoMapper();
    private final co.edu.unbosque.repository.interfaces.IPrestamoRepository prestamos =
            new co.edu.unbosque.repository.implementations.PrestamoRepositoryJDBC();

    /** Estados que solo asignan los préstamos: interno = En mesa, externo = Prestado. */
    private static final Set<String> ESTADOS_DE_PRESTAMO = Set.of(PRESTADO, EN_MESA);

    /** Convierte el estado recibido al formato guardado (mayúsculas, sin espacios). */
    static String normalizarEstado(String estado) {
        if (estado == null) {
            return null;
        }
        String valor = estado.trim().toUpperCase().replace(' ', '_');
        return switch (valor) {
            case "EN_USO", "EN_USO_EN_MESA" -> EN_MESA;
            case "REPORTADO_CON_FALTANTES", "INCOMPLETO" -> FALTANTES;
            case "DAÑADO", "DANADO", "EN_REPARACION", "EN_REPARACIÓN" -> MANTENIMIENTO;
            default -> valor;
        };
    }

    private void validarUnidadJuego(UnidadJuegoRequestDTO request) {
        Validaciones.requerido(request, "Debes enviar los datos de la unidad.");
        Validaciones.texto(request.getCodigo(), "código");
        Validaciones.texto(request.getNumSerie(), "número de serie");
        Validaciones.texto(request.getEstado(), "estado");
        String estado = normalizarEstado(request.getEstado());
        if (!ESTADOS.contains(estado)) {
            throw BusinessException.badRequest(
                    "Estado inválido. Usa: Disponible, En mesa, Prestado, Mantenimiento o Faltantes.");
        }
        request.setEstado(estado);
    }

    /**
     * Código sugerido para una unidad nueva, con el formato que ya usa el negocio:
     * UJ-<primera parte del número de serie>-<consecutivo de 3 dígitos>. Ej: UJ-CATAN-002.
     */
    String generarCodigo(String numSerie) {
        String base = numSerie.trim().split("-")[0].toUpperCase().replaceAll("[^A-Z0-9]", "");
        if (base.isEmpty()) base = "JUEGO";
        if (base.length() > 20) base = base.substring(0, 20);
        int consecutivo = unidadJuegoRepository.findAll(numSerie.trim()).size() + 1;
        String codigo;
        do {
            codigo = String.format("UJ-%s-%03d", base, consecutivo++);
        } while (unidadJuegoRepository.findByCodigo(codigo, numSerie.trim()).isPresent());
        return codigo;
    }

    @Override
    public UnidadJuegoResponseDTO addUnidadJuego(UnidadJuegoRequestDTO request) {
        Validaciones.requerido(request, "Debes enviar los datos de la unidad.");
        Validaciones.texto(request.getNumSerie(), "juego");
        request.setNumSerie(request.getNumSerie().trim());
        if (request.getCodigo() == null || request.getCodigo().isBlank()) {
            request.setCodigo(generarCodigo(request.getNumSerie()));
        }
        request.setCodigo(request.getCodigo().trim());
        validarUnidadJuego(request);
        if (ESTADOS_DE_PRESTAMO.contains(request.getEstado())) {
            throw BusinessException.badRequest(
                    "Una unidad nueva no puede quedar prestada ni en mesa; eso lo asigna el módulo de préstamos.");
        }
        if (unidadJuegoRepository.findByCodigo(request.getCodigo(), request.getNumSerie()).isPresent()) {
            throw BusinessException.conflict("Ya existe una unidad con ese código para ese juego.");
        }

        UnidadJuego unidadJuego = unidadJuegoMapper.unidadJuegoRequestDTOToUnidadJuego(request);

        if (!unidadJuegoRepository.saveUnidadJuego(unidadJuego)) {
            return null;
        }

        return unidadJuegoMapper.unidadJuegoToUnidadJuegoResponseDTO(unidadJuego);
    }

    @Override
    public UnidadJuegoResponseDTO findByCodigo(String codigo, String numSerie) {
        Optional<UnidadJuego> unidad = unidadJuegoRepository.findByCodigo(codigo, numSerie);
        return unidad.map(unidadJuegoMapper::unidadJuegoToUnidadJuegoResponseDTO).orElse(null);
    }

    @Override
    public UnidadJuegoResponseDTO updateUnidadJuego(String codigo, String numSerie, UnidadJuegoRequestDTO request) {
        validarUnidadJuego(request);

        Optional<UnidadJuego> actual = unidadJuegoRepository.findByCodigo(codigo, numSerie);
        if (actual.isEmpty()) {
            return null;
        }

        // Prestado y En mesa solo los manejan el registro de préstamo y la devolución.
        String estadoActual = normalizarEstado(actual.get().getEstado());
        boolean estabaEnPrestamo = ESTADOS_DE_PRESTAMO.contains(estadoActual);
        boolean quedaEnPrestamo = ESTADOS_DE_PRESTAMO.contains(request.getEstado());
        if (quedaEnPrestamo && !request.getEstado().equals(estadoActual)) {
            throw BusinessException.conflict(
                    "Para prestar una unidad o ponerla en mesa usa el módulo de préstamos.");
        }
        if (estabaEnPrestamo && !quedaEnPrestamo && prestamos.unidadEnPrestamoActivo(codigo, numSerie)) {
            throw BusinessException.conflict(
                    "La unidad está en un préstamo sin devolver. Registra la devolución en el módulo de préstamos.");
        }

        UnidadJuego unidadJuego = unidadJuegoMapper.unidadJuegoRequestDTOToUnidadJuego(request);

        if (!unidadJuegoRepository.updateUnidadJuego(codigo, numSerie, unidadJuego)) {
            return null;
        }

        return unidadJuegoRepository.findByCodigo(codigo, numSerie)
                .map(unidadJuegoMapper::unidadJuegoToUnidadJuegoResponseDTO)
                .orElse(null);
    }

    @Override
    public UnidadJuegoResponseDTO deleteUnidadJuego(String codigo, String numSerie) {
        Optional<UnidadJuego> unidad = unidadJuegoRepository.findByCodigo(codigo, numSerie);

        if (unidad.isEmpty()) {
            return null;
        }
        if (prestamos.unidadEnPrestamoActivo(codigo, numSerie)) {
            throw BusinessException.conflict("No se puede eliminar una unidad que está en un préstamo sin devolver.");
        }

        if (!unidadJuegoRepository.deleteUnidadJuego(codigo, numSerie)) {
            return null;
        }

        return unidadJuegoMapper.unidadJuegoToUnidadJuegoResponseDTO(unidad.get());
    }

    @Override
    public List<UnidadJuegoResponseDTO> listarDisponibles() {
        return unidadJuegoRepository.findDisponibles().stream()
                .map(unidadJuegoMapper::unidadJuegoToUnidadJuegoResponseDTO).toList();
    }

    @Override
    public List<UnidadJuegoResponseDTO> listar(String numSerie) {
        return unidadJuegoRepository.findAll(numSerie).stream()
                .map(unidadJuegoMapper::unidadJuegoToUnidadJuegoResponseDTO).toList();
    }

    @Override
    public List<DisponibilidadUnidadJuegoDTO> consultarDisponibilidad() {
        return unidadJuegoRepository.consultarDisponibilidad();
    }
}
