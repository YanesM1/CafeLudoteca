package co.edu.unbosque.services.implementations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import co.edu.unbosque.mapper.JuegoMesaMapper;
import co.edu.unbosque.model.dto.JuegoMesaRequestDTO;
import co.edu.unbosque.model.dto.JuegoMesaResponseDTO;
import co.edu.unbosque.model.entity.JuegoMesa;
import co.edu.unbosque.repository.implementations.JuegoMesaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IJuegoMesaRepository;
import co.edu.unbosque.services.interfaces.IJuegoMesaService;


public class JuegoMesaService implements IJuegoMesaService {

    private final IJuegoMesaRepository juegoMesaRepository = new JuegoMesaRepositoryJDBC();
    private final JuegoMesaMapper juegoMesaMapper = new JuegoMesaMapper();

    @Override
    public JuegoMesaResponseDTO addJuegoMesa(JuegoMesaRequestDTO request) {

        if (request == null) {
            return null;
        }

        JuegoMesa juegoMesa = juegoMesaMapper.juegoMesaRequestDTOToJuegoMesa(request);

        if (!juegoMesaRepository.saveJuegoMesa(juegoMesa)) {
            return null;
        }

        return juegoMesaMapper.juegoMesaToResponseDTO(juegoMesa);
 }

    @Override
    public List<JuegoMesaResponseDTO> getJuegoMesa(
            String nombre) {

        List<JuegoMesa> juegos = juegoMesaRepository.findByNombre(nombre);
        List<JuegoMesaResponseDTO> response = new ArrayList<>();

        for (JuegoMesa juegoM : juegos) {
            response.add(
                    juegoMesaMapper.juegoMesaToResponseDTO(juegoM));
        }

        return response;
    }

    @Override
    public JuegoMesaResponseDTO getJuegosPorNumSerie(
            String numSerie) {

        Optional<JuegoMesa> juego = juegoMesaRepository.findByNumSerie(numSerie);

        if (juego.isEmpty()) {
            return null;
        }

        return juegoMesaMapper.juegoMesaToResponseDTO(
                juego.get());
    }

    @Override
    public JuegoMesaResponseDTO updateJuegoMesa(
            String numSerie,
            JuegoMesaRequestDTO request) {

        if (request == null) {
            return null;
        }

        JuegoMesa juegoMesa = juegoMesaMapper.juegoMesaRequestDTOToJuegoMesa(request);

        if (!juegoMesaRepository.updateJuegoMesa(
                numSerie,
                juegoMesa)) {
            return null;
        }

        Optional<JuegoMesa> actualizado = juegoMesaRepository.findByNumSerie(numSerie);

        if (actualizado.isEmpty()) {
            return null;
        }

        return juegoMesaMapper.juegoMesaToResponseDTO(
                actualizado.get());
    }

    @Override
    public JuegoMesaResponseDTO deleteJuegoMesa(
            String numSerie) {

        Optional<JuegoMesa> juego = juegoMesaRepository.findByNumSerie(numSerie);

        if (juego.isEmpty()) {
            return null;
        }

        if (!juegoMesaRepository.deleteJuegoMesa(numSerie)) {
            return null;
        }

        return juegoMesaMapper.juegoMesaToResponseDTO(
                juego.get());
    }
}