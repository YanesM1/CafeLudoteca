package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.TelefonoRequestDTO;
import jakarta.ws.rs.core.Response;

public interface ITelefonoController {

    Response guardarTelefono(TelefonoRequestDTO request);

    Response buscarPorDocumento(String documentoCliente);

    Response actualizarTelefono(String documentoCliente, String telefono, TelefonoRequestDTO request);

    Response borrarTelefono(String documentoCliente, String telefono);
}
