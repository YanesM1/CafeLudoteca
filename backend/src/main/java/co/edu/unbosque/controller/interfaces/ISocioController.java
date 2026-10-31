package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.SocioRequestDTO;
import jakarta.ws.rs.core.Response;

public interface ISocioController {
    Response guardarSocio(SocioRequestDTO request);
    Response buscarPorFechaAfiliacion(String fechaAfiliacion);
    Response buscarPorDocumento(String documento);
    Response actualizarSocio(String documento, SocioRequestDTO request);
    Response borrarSocio(String documento);

}