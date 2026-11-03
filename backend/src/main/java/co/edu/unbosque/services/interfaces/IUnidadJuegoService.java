package co.edu.unbosque.services.interfaces;

import co.edu.unbosque.model.dto.DisponibilidadUnidadJuegoDTO;
import java.util.List;
import co.edu.unbosque.model.dto.UnidadJuegoRequestDTO;
import co.edu.unbosque.model.dto.UnidadJuegoResponseDTO;

public interface IUnidadJuegoService {
    UnidadJuegoResponseDTO addUnidadJuego(UnidadJuegoRequestDTO request);

    UnidadJuegoResponseDTO findByCodigo(String codigo, String numSerie);

    UnidadJuegoResponseDTO updateUnidadJuego(String codigo, String numSerie, UnidadJuegoRequestDTO request);

    UnidadJuegoResponseDTO deleteUnidadJuego(String codigo, String numSerie);

    List<DisponibilidadUnidadJuegoDTO> consultarDisponibilidad();
    List<UnidadJuegoResponseDTO> listarDisponibles();
}