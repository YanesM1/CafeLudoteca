package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.ClienteRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IClienteController {
    Response guardarCliente(ClienteRequestDTO request);
    Response buscarPorNombre(String nombre);
    Response buscarPorDocumento(String documento);
    Response actualizarCliente(String documento, ClienteRequestDTO request);
    Response borrarCliente(String documento);

}