
package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
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

    // VALIDAR DATOS DE LA MESA
    private boolean validarMesa(
            MesaRequestDTO request,
            boolean validarId) {

        if (request == null) {
            return false;
        }

        if (validarId &&
                (request.getIdMesa() == null ||
                 request.getIdMesa().isBlank())) {
            return false;
        }

        if (request.getCapacidad() <= 0) {
            return false;
        }

        if (request.getTematica() == null ||
                request.getTematica().isBlank()) {
            return false;
        }

        if (request.getUbicacion() == null ||
                request.getUbicacion().isBlank()) {
            return false;
        }

        return true;
    }

    // REGISTRAR MESA
    @Override
    public MesaResponseDTO addMesa(MesaRequestDTO request) {

        if (!validarMesa(request, true)) {
            return null;
        }

        Mesa mesa = MesaMapper.toEntity(request);

        if (!mesaRepository.saveMesa(mesa)) {
            return null;
        }

        return MesaMapper.toResponse(mesa);
    }

    // CONSULTAR MESAS POR TEMATICA
    @Override
    public List<MesaResponseDTO> getMesa(String tematica) {

        if (tematica == null || tematica.isBlank()) {
            return new ArrayList<>();
        }

        List<Mesa> mesas =
                mesaRepository.findByTematica(tematica.trim());

        return MesaMapper.toResponseList(mesas);
    }

    // CONSULTAR MESA POR IDENTIFICADOR
    @Override
    public MesaResponseDTO getMesasPorIdMesa(String idMesa) {

        if (idMesa == null || idMesa.isBlank()) {
            return null;
        }

        Optional<Mesa> mesa =
                mesaRepository.findByIdMesa(idMesa.trim());

        return mesa
                .map(MesaMapper::toResponse)
                .orElse(null);
    }

    // ACTUALIZAR MESA
    @Override
    public MesaResponseDTO updateMesa(
            String idMesa,
            MesaRequestDTO request) {

        if (idMesa == null || idMesa.isBlank()) {
            return null;
        }

        if (!validarMesa(request, false)) {
            return null;
        }

        // Evitar modificar accidentalmente la clave primaria
        if (request.getIdMesa() != null &&
                !request.getIdMesa().isBlank() &&
                !idMesa.equals(request.getIdMesa())) {
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

    // ELIMINAR MESA
    @Override
    public MesaResponseDTO deleteMesa(String idMesa) {

        if (idMesa == null || idMesa.isBlank()) {
            return null;
        }

        Optional<Mesa> mesa =
                mesaRepository.findByIdMesa(idMesa);

        if (mesa.isEmpty()) {
            return null;
        }

        if (!mesaRepository.deleteMesa(idMesa)) {
            return null;
        }

        return MesaMapper.toResponse(mesa.get());
    }
}
