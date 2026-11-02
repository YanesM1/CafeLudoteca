package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.ClienteFrecuenteRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IClienteFrecuenteController {
    Response guardarClienteFrecuente(ClienteFrecuenteRequestDTO request);
    Response buscarPorNivelFidelidad(String nivelFidelidad);
    Response buscarPorDocumento(String documento);
    Response actualizarClienteFrecuente(String documento, ClienteFrecuenteRequestDTO request);
    Response borrarClienteFrecuente(String documento);

}