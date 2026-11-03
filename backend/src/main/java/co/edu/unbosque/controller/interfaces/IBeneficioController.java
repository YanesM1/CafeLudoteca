package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IBeneficioController {
    Response guardarBeneficio(BeneficioRequestDTO request);
    Response obtenerBeneficios();
    Response buscarPorId(Integer idBeneficio, Integer idMembresia, String documentoSocio);
    Response actualizarBeneficio(Integer idBeneficio, Integer idMembresia, String documentoSocio, BeneficioRequestDTO request);
    Response borrarBeneficio(Integer idBeneficio, Integer idMembresia, String documentoSocio);
}
