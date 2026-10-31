package co.edu.unbosque.services.interfaces;

import co.edu.unbosque.model.dto.UnidadJuegoRequestDTO;
import co.edu.unbosque.model.dto.UnidadJuegoResponseDTO;

public interface IUnidadJuegoService {
    UnidadJuegoResponseDTO addUnidadJuego(UnidadJuegoRequestDTO request);
    UnidadJuegoResponseDTO findByCodigo(String codigo);
    UnidadJuegoResponseDTO updateUnidadJuego(String codigo, UnidadJuegoRequestDTO request);
    UnidadJuegoResponseDTO deleteUnidadJuego(String codigo);

}
