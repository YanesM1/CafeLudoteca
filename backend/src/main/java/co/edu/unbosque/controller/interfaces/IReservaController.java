
package co.edu.unbosque.controller.interfaces;

import jakarta.ws.rs.core.Response;

import co.edu.unbosque.model.dto.ReservaRequestDTO;

public interface IReservaController {

    Response crearReserva(ReservaRequestDTO request);

    Response buscarReservas(String documento);

    Response obtenerReserva(String idReserva);

    Response actualizarReserva(
            String idReserva,
            ReservaRequestDTO request
    );

    Response eliminarReserva(String idReserva);

}
