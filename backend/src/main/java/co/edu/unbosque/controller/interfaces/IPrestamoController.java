package co.edu.unbosque.controller.interfaces;
import co.edu.unbosque.model.dto.PrestamoRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IPrestamoController {
        Response guardarPrestamo(PrestamoRequestDTO request);

    Response buscarPorTipoPrestamo(String tipoPrestamo);

    Response buscarPorIdPrestamo(Integer idPrestamo);

    Response actualizarPrestamo(Integer idPrestamo, PrestamoRequestDTO request);

    Response borrarPrestamo(Integer idPrestamo);

}
