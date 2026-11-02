package co.edu.unbosque.services.interfaces;
import co.edu.unbosque.model.dto.ElementoJuegoRequestDTO;
import co.edu.unbosque.model.dto.ElementoJuegoResponseDTO;


public interface IElementoJuegoService {
    ElementoJuegoResponseDTO addElementoJuego(ElementoJuegoRequestDTO request);

    ElementoJuegoResponseDTO getPorIdElementoJuego(Integer idElementoJuego);

    ElementoJuegoResponseDTO updateElementoJuego(Integer idElementoJuego, ElementoJuegoRequestDTO request);

    ElementoJuegoResponseDTO deleteElementoJuego(Integer idElementoJuego);

}
