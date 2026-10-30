package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.JuegoMesaRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IJuegoMesaController {
    Response guardarJuego(JuegoMesaRequestDTO request);
    Response buscarPorNombre(String nombre);
    Response buscarPorNumSerie(String numSerie);
    Response actualizarJuegoMesa(String numSerie, JuegoMesaRequestDTO request);
    Response borrarJuegoMesa(String numSerie);
    


}
