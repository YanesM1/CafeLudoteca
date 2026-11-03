package co.edu.unbosque.services.implementations;

import java.util.*;
import co.edu.unbosque.mapper.MembresiaMapper;
import co.edu.unbosque.model.dto.*;
import co.edu.unbosque.model.entity.Membresia;
import co.edu.unbosque.repository.implementations.MembresiaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IMembresiaRepository;
import co.edu.unbosque.services.interfaces.IMembresiaService;

public class MembresiaService implements IMembresiaService {
    private final IMembresiaRepository repository = new MembresiaRepositoryJDBC();
    private final MembresiaMapper mapper = new MembresiaMapper();

    private boolean valido(MembresiaRequestDTO r) {
        return r != null && r.getIdMembresia() > 0
            && r.getDocumento() != null && !r.getDocumento().isBlank()
            && r.getPlan() != null && !r.getPlan().isBlank()
            && r.getEstado() != null && !r.getEstado().isBlank()
            && r.getFechaInicio() != null
            && (r.getFechaFin() == null || !r.getFechaFin().isBefore(r.getFechaInicio()))
            && r.getTarifa() > 0;
    }

    private List<MembresiaResponseDTO> convertir(List<Membresia> lista) {
        List<MembresiaResponseDTO> salida = new ArrayList<>();
        for (Membresia m : lista) salida.add(mapper.membresiaToMembresiaResponseDTO(m));
        return salida;
    }

    @Override
    public MembresiaResponseDTO addMembresia(MembresiaRequestDTO r) {
        if (!valido(r)) return null;
        Membresia m = mapper.membresiaRequestDTOToMembresia(r);
        return repository.saveMembresia(m) ? mapper.membresiaToMembresiaResponseDTO(m) : null;
    }

    @Override
    public List<MembresiaResponseDTO> getTodas() {
        return convertir(repository.findAll());
    }

    @Override
    public List<MembresiaResponseDTO> getMembresiaPorDocumento(String documento) {
        if (documento == null || documento.isBlank()) return List.of();
        return convertir(repository.findByDocumento(documento));
    }

    @Override
    public List<MembresiaResponseDTO> getMembresiaPorEstado(String estado) {
        if (estado == null || estado.isBlank()) return List.of();
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
        if (id <= 0 || documento == null || documento.isBlank() || !valido(r)) return null;
        if (!documento.equals(r.getDocumento())) return null;
        Membresia actual = mapper.membresiaRequestDTOToMembresia(r);
        if (!repository.updateMembresia(id, documento, actual)) return null;
        return getMembresiaPorId(id, documento);
    }

    @Override
    public MembresiaResponseDTO deleteMembresia(int id, String documento) {
        MembresiaResponseDTO existente = getMembresiaPorId(id, documento);
        if (existente == null || !repository.deleteMembresia(id, documento)) return null;
        return existente;
    }
}
