package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IMembresiaController {
    Response guardarMembresia(MembresiaRequestDTO request);
    Response buscarPorDocumento(String documento);
    Response buscarPorEstado(String estado);
    Response buscarPorId(String idMembresia);
    Response actualizarMembresia(String idMembresia, MembresiaRequestDTO request);
    Response borrarMembresia(String idMembresia);

}