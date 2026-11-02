package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.ExpansionRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IExpansionController {
    Response guardarExpansion(ExpansionRequestDTO request);

    Response buscarPorNombre(String nombre);

    Response buscarPorIdExpansion(Integer idExpansion);

    Response actualizarExpansion(Integer idExpansion, ExpansionRequestDTO request);

    Response borrarJuegoMesa(Integer idExpansion);

}
