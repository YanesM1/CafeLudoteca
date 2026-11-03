
package co.edu.unbosque.controller.interfaces;

import jakarta.ws.rs.core.Response;

import co.edu.unbosque.model.dto.MesaRequestDTO;

public interface IMesaController {

    Response crearMesa(MesaRequestDTO request);

    Response listarMesas();

    Response disponibilidadMesas(String fecha, String horario);

    Response buscarMesas(String tematica);

    Response obtenerMesa(String idMesa);

    Response actualizarMesa(
            String idMesa,
            MesaRequestDTO request
    );

    Response eliminarMesa(String idMesa);

}
