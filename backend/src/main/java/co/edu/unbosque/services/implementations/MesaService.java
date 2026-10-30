
package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.MesaMapper;
import co.edu.unbosque.model.dto.MesaRequestDTO;
import co.edu.unbosque.model.dto.MesaResponseDTO;
import co.edu.unbosque.model.entity.Mesa;
import co.edu.unbosque.repository.implementations.MesaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IMesaRepository;
import co.edu.unbosque.services.interfaces.IMesaService;

public class MesaService implements IMesaService {

    private final IMesaRepository mesaRepository =
            new MesaRepositoryJDBC();

    @Override
    public MesaResponseDTO addMesa(MesaRequestDTO request) {
        if (request == null) {
            return null;
        }

        Mesa mesa = MesaMapper.toEntity(request);

        if (!mesaRepository.saveMesa(mesa)) {
            return null;
        }

        return MesaMapper.toResponse(mesa);
    }

    @Override
    public List<MesaResponseDTO> getMesa(String tematica) {
        List<Mesa> mesas = mesaRepository.findByTematica(tematica);

        return MesaMapper.toResponseList(mesas);
    }

    @Override
    public MesaResponseDTO getMesasPorIdMesa(String idMesa) {
        Optional<Mesa> mesa = mesaRepository.findByIdMesa(idMesa);

        return mesa
                .map(MesaMapper::toResponse)
                .orElse(null);
    }

    @Override
    public MesaResponseDTO updateMesa(
            String idMesa,
            MesaRequestDTO request) {

        if (idMesa == null || request == null) {
            return null;
        }

        Mesa mesa = MesaMapper.toEntity(request);

        if (!mesaRepository.updateMesa(idMesa, mesa)) {
            return null;
        }

        return mesaRepository.findByIdMesa(idMesa)
                .map(MesaMapper::toResponse)
                .orElse(null);
    }

    @Override
    public MesaResponseDTO deleteMesa(String idMesa) {
        Optional<Mesa> mesa = mesaRepository.findByIdMesa(idMesa);

        if (mesa.isEmpty()) {
            return null;
        }

        if (!mesaRepository.deleteMesa(idMesa)) {
            return null;
        }

        return MesaMapper.toResponse(mesa.get());
    }
}
