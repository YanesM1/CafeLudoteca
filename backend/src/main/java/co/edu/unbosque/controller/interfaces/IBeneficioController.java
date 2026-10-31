package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import jakarta.ws.rs.core.Response;

public interface IBeneficioController {
    Response guardarBeneficio(BeneficioRequestDTO request);
    Response buscarPorMembresia(String idMembresia);
    Response buscarPorId(String idMembresia, String idBeneficio);
    Response actualizarBeneficio(String idMembresia, String idBeneficio, BeneficioRequestDTO request);
    Response borrarBeneficio(String idMembresia, String idBeneficio);

}