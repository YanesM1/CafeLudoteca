package co.edu.unbosque.services.implementations;

import co.edu.unbosque.model.dto.DisponibilidadUnidadJuegoDTO;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.UnidadJuegoMapper;
import co.edu.unbosque.model.dto.UnidadJuegoRequestDTO;
import co.edu.unbosque.model.dto.UnidadJuegoResponseDTO;
import co.edu.unbosque.model.entity.UnidadJuego;
import co.edu.unbosque.repository.implementations.UnidadJuegoRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IUnidadJuegoRepository;
import co.edu.unbosque.services.interfaces.IUnidadJuegoService;

public class UnidadJuegoService implements IUnidadJuegoService {

    private final IUnidadJuegoRepository unidadJuegoRepository = new UnidadJuegoRepositoryJDBC();
    private final UnidadJuegoMapper unidadJuegoMapper = new UnidadJuegoMapper();

    private boolean validarUnidadJuego(UnidadJuegoRequestDTO request) {

        if (request == null) {
            return false;
        }

        if (request.getCodigo() == null || request.getCodigo().isBlank()) {
            return false;
        }

        if (request.getNumSerie() == null || request.getNumSerie().isBlank()) {
            return false;
        }

        if (request.getEstado() == null || request.getEstado().isBlank()) {
            return false;
        }

        return true;
    }

    @Override
    public UnidadJuegoResponseDTO addUnidadJuego(
            UnidadJuegoRequestDTO request) {

        if (!validarUnidadJuego(request)) {
            return null;
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

        if (unidad.isEmpty()) {
            return null;
        }

        return unidadJuegoMapper.unidadJuegoToUnidadJuegoResponseDTO(unidad.get());
    }

    @Override
    public UnidadJuegoResponseDTO updateUnidadJuego(String codigo, String numSerie, UnidadJuegoRequestDTO request) {

        if (!validarUnidadJuego(request)) {
            return null;
        }

        UnidadJuego unidadJuego = unidadJuegoMapper.unidadJuegoRequestDTOToUnidadJuego(request);

        if (!unidadJuegoRepository.updateUnidadJuego(codigo, numSerie, unidadJuego)) {
            return null;
        }

        Optional<UnidadJuego> actualizada = unidadJuegoRepository.findByCodigo(codigo, numSerie);

        if (actualizada.isEmpty()) {
            return null;
        }

        return unidadJuegoMapper.unidadJuegoToUnidadJuegoResponseDTO(actualizada.get());
    }

    @Override
    public UnidadJuegoResponseDTO deleteUnidadJuego(String codigo, String numSerie) {

        Optional<UnidadJuego> unidad = unidadJuegoRepository.findByCodigo(codigo, numSerie);

        if (unidad.isEmpty()) {
            return null;
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
    public List<DisponibilidadUnidadJuegoDTO> consultarDisponibilidad() {
        return unidadJuegoRepository.consultarDisponibilidad();
    }

}