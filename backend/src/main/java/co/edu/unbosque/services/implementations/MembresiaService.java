package co.edu.unbosque.services.implementations;

import java.time.LocalDate;
import java.util.*;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.mapper.MembresiaMapper;
import co.edu.unbosque.model.dto.*;
import co.edu.unbosque.model.entity.Membresia;
import co.edu.unbosque.repository.implementations.MembresiaRepositoryJDBC;
import co.edu.unbosque.repository.implementations.SocioRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IMembresiaRepository;
import co.edu.unbosque.repository.interfaces.ISocioRepository;
import co.edu.unbosque.services.PlanesMembresia;
import co.edu.unbosque.services.interfaces.IMembresiaService;

/**
 * Membresías de socios (RF-13, RF-14):
 * - El número de membresía se asigna automáticamente.
 * - El plan debe ser uno de los definidos. La tarifa se toma del plan al contratar y queda
 *   registrada como precio pactado (al editar solo cambia si cambia el plan).
 * - El socio debe existir y solo puede tener una membresía Activa vigente.
 * - Las membresías Activas cuya fecha de fin ya pasó quedan Vencidas.
 */
public class MembresiaService implements IMembresiaService {
    private final IMembresiaRepository repository = new MembresiaRepositoryJDBC();
    private final ISocioRepository socios = new SocioRepositoryJDBC();
    private final MembresiaMapper mapper = new MembresiaMapper();

    /** Valida y normaliza los datos; devuelve la entidad lista para guardar. */
    private Membresia preparar(MembresiaRequestDTO r) {
        if (r == null) throw BusinessException.badRequest("Debes enviar los datos de la membresía.");
        if (r.getDocumento() == null || r.getDocumento().isBlank())
            throw BusinessException.badRequest("Selecciona el socio.");

        String plan = PlanesMembresia.plan(r.getPlan());
        if (plan == null)
            throw BusinessException.badRequest("El plan debe ser Premium, Alta, Media, Básica o Baja.");

        String estado = r.getEstado() == null || r.getEstado().isBlank()
            ? PlanesMembresia.ACTIVA : PlanesMembresia.estado(r.getEstado());
        if (estado == null)
            throw BusinessException.badRequest("El estado debe ser Activa, Suspendida o Vencida.");

        if (r.getFechaInicio() == null)
            throw BusinessException.badRequest("La fecha de inicio es obligatoria.");
        if (r.getFechaFin() == null)
            throw BusinessException.badRequest("La fecha de fin es obligatoria.");
        if (!r.getFechaFin().isAfter(r.getFechaInicio()))
            throw BusinessException.badRequest("La fecha de fin debe ser posterior a la de inicio.");

        // Una membresía con fecha de fin pasada no puede quedar Activa
        if (PlanesMembresia.ACTIVA.equals(estado) && r.getFechaFin().isBefore(LocalDate.now()))
            estado = PlanesMembresia.VENCIDA;

        Membresia m = mapper.membresiaRequestDTOToMembresia(r);
        m.setDocumento(r.getDocumento().trim());
        m.setPlan(plan);
        m.setEstado(estado);
        m.setTarifa(PlanesMembresia.tarifa(plan));
        return m;
    }

    private void validarUnaActiva(Membresia m, int excluirId) {
        if (PlanesMembresia.ACTIVA.equals(m.getEstado())
                && repository.tieneOtraActiva(m.getDocumento(), excluirId)) {
            throw BusinessException.conflict(
                "El socio ya tiene una membresía activa. Suspéndela o espera a que venza antes de crear otra.");
        }
    }

    private List<MembresiaResponseDTO> convertir(List<Membresia> lista) {
        List<MembresiaResponseDTO> salida = new ArrayList<>();
        for (Membresia m : lista) salida.add(mapper.membresiaToMembresiaResponseDTO(m));
        return salida;
    }

    @Override
    public List<Map<String, Object>> getPlanes() {
        List<Map<String, Object>> planes = new ArrayList<>();
        PlanesMembresia.TARIFAS.forEach((plan, tarifa) -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("plan", plan);
            item.put("tarifa", tarifa);
            planes.add(item);
        });
        return planes;
    }

    @Override
    public MembresiaResponseDTO addMembresia(MembresiaRequestDTO r) {
        Membresia m = preparar(r);
        if (socios.findByDocumento(m.getDocumento()).isEmpty())
            throw BusinessException.notFound("El documento " + m.getDocumento() + " no corresponde a un socio registrado.");
        validarUnaActiva(m, 0);
        m.setIdMembresia(repository.siguienteId());
        if (!repository.saveMembresia(m))
            throw BusinessException.badRequest("No fue posible registrar la membresía.");
        return mapper.membresiaToMembresiaResponseDTO(m);
    }

    @Override
    public List<MembresiaResponseDTO> getTodas() {
        repository.vencerMembresias();
        return convertir(repository.findAll());
    }

    @Override
    public List<MembresiaResponseDTO> getMembresiaPorDocumento(String documento) {
        if (documento == null || documento.isBlank()) return List.of();
        repository.vencerMembresias();
        return convertir(repository.findByDocumento(documento));
    }

    @Override
    public List<MembresiaResponseDTO> getMembresiaPorEstado(String estado) {
        if (estado == null || estado.isBlank()) return List.of();
        repository.vencerMembresias();
        return convertir(repository.findByEstado(estado));
    }

    @Override
    public MembresiaResponseDTO getMembresiaPorId(int id, String documento) {
        if (id <= 0 || documento == null || documento.isBlank()) return null;
        return repository.findByIdMembresia(id, documento)
            .map(mapper::membresiaToMembresiaResponseDTO).orElse(null);
    }

    @Override
    public MembresiaResponseDTO updateMembresia(int id, String documento, MembresiaRequestDTO r) {
        if (id <= 0 || documento == null || documento.isBlank())
            throw BusinessException.badRequest("Membresía inválida.");
        Membresia actual = repository.findByIdMembresia(id, documento)
            .orElseThrow(() -> BusinessException.notFound("La membresía #" + id + " no existe."));
        if (r != null) r.setDocumento(documento);
        Membresia m = preparar(r);
        m.setIdMembresia(id);
        // La tarifa es el precio pactado al contratar: si el plan no cambia, se conserva
        if (m.getPlan().equals(PlanesMembresia.plan(actual.getPlan()))) {
            m.setTarifa(actual.getTarifa());
        }
        validarUnaActiva(m, id);
        if (!repository.updateMembresia(id, documento, m))
            throw BusinessException.badRequest("No fue posible actualizar la membresía.");
        return getMembresiaPorId(id, documento);
    }

    @Override
    public MembresiaResponseDTO deleteMembresia(int id, String documento) {
        MembresiaResponseDTO existente = getMembresiaPorId(id, documento);
        if (existente == null || !repository.deleteMembresia(id, documento)) return null;
        return existente;
    }
}
