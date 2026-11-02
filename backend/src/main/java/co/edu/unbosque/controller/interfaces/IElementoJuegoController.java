package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.ElementoJuegoRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IElementoJuegoController {
    Response guardarElementoJuego(ElementoJuegoRequestDTO request);

    Response buscarPorIdElementoJuego(Integer idElementoJuego);

    Response actualizarElementoJuego(Integer idElementoJuego, ElementoJuegoRequestDTO request);

    Response borrarElementoJuego(Integer idElementoJuego);

}
