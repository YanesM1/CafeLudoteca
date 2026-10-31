package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;

public interface IBeneficioService {

    BeneficioResponseDTO addBeneficio(BeneficioRequestDTO request);

    List<BeneficioResponseDTO> getBeneficiosPorMembresia(String idMembresia);

    BeneficioResponseDTO getBeneficioPorId(String idMembresia, String idBeneficio);

    BeneficioResponseDTO updateBeneficio(
            String idMembresia,
            String idBeneficio,
            BeneficioRequestDTO request
    );

    BeneficioResponseDTO deleteBeneficio(String idMembresia, String idBeneficio);

}