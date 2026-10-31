package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.MembresiaMapper;
import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import co.edu.unbosque.model.dto.MembresiaResponseDTO;
import co.edu.unbosque.model.entity.Membresia;
import co.edu.unbosque.repository.implementations.MembresiaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IMembresiaRepository;
import co.edu.unbosque.services.interfaces.IMembresiaService;

public class MembresiaService implements IMembresiaService {

    private final IMembresiaRepository membresiaRepository =
            new MembresiaRepositoryJDBC();

    @Override
    public MembresiaResponseDTO addMembresia(MembresiaRequestDTO request) {
        if (request == null) {
            return null;
        }

        Membresia membresia = MembresiaMapper.toEntity(request);

        if (!membresiaRepository.saveMembresia(membresia)) {
            return null;
        }

        return MembresiaMapper.toResponse(membresia);
    }

    @Override
    public List<MembresiaResponseDTO> getMembresiasPorDocumento(String documento) {
        List<Membresia> membresias =
                membresiaRepository.findByDocumento(documento);

        return MembresiaMapper.toResponseList(membresias);
    }

    @Override
    public List<MembresiaResponseDTO> getMembresiasPorEstado(String estado) {
        List<Membresia> membresias =
                membresiaRepository.findByEstado(estado);

        return MembresiaMapper.toResponseList(membresias);
    }

    @Override
    public MembresiaResponseDTO getMembresiaPorId(String idMembresia) {
        Optional<Membresia> membresia =
                membresiaRepository.findByIdMembresia(idMembresia);

        return membresia
                .map(MembresiaMapper::toResponse)
                .orElse(null);
    }

    @Override
    public MembresiaResponseDTO updateMembresia(
            String idMembresia,
            MembresiaRequestDTO request) {

        if (idMembresia == null || request == null) {
            return null;
        }

        Membresia membresia = MembresiaMapper.toEntity(request);

        if (!membresiaRepository.updateMembresia(idMembresia, membresia)) {
            return null;
        }

        return membresiaRepository.findByIdMembresia(idMembresia)
                .map(MembresiaMapper::toResponse)
                .orElse(null);
    }

    @Override
    public MembresiaResponseDTO deleteMembresia(String idMembresia) {
        Optional<Membresia> membresia =
                membresiaRepository.findByIdMembresia(idMembresia);

        if (membresia.isEmpty()) {
            return null;
        }

        if (!membresiaRepository.deleteMembresia(idMembresia)) {
            return null;
        }

        return MembresiaMapper.toResponse(membresia.get());
    }
}