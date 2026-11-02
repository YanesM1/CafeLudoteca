package co.edu.unbosque.controller.interfaces;
import co.edu.unbosque.model.dto.UnidadJuegoRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IUnidadJuegoController {
    Response guardarUnidad(UnidadJuegoRequestDTO request);
    Response buscarPorCodigo(String codigo, String numSerie);
    Response actualizarUnidadJuego(String codigo, String numSerie, UnidadJuegoRequestDTO request);
    Response borrarUnidadJuego(String codigo, String numSerie);

}
