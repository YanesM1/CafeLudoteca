package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IBeneficioController {
    Response guardarBeneficio(BeneficioRequestDTO request);
    Response obtenerBeneficios();
    Response buscarPorId( Integer idBeneficio, Integer idMembresia);
    Response actualizarBeneficio(Integer idBeneficio, Integer idMembresia, BeneficioRequestDTO request);
    Response borrarBeneficio( Integer idBenefio, Integer idMembresia);
}