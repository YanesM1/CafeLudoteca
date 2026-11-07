package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IMembresiaController {
    Response guardarMembresia(MembresiaRequestDTO request);
    Response listar();
    Response planes();
    Response buscarPorDocumento(String documento);
    Response buscarPorEstado(String estado);
    Response buscarPorId(int idMembresia, String documento);
    Response actualizarMembresia(int idMembresia, String documento, MembresiaRequestDTO request);
    Response borrarMembresia(int idMembresia, String documento);
}
