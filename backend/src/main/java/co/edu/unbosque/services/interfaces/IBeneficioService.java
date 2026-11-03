package co.edu.unbosque.services.interfaces;

import java.util.List;
import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;

public interface IBeneficioService {
    BeneficioResponseDTO addBeneficio(BeneficioRequestDTO request);
    List<BeneficioResponseDTO> getBeneficios();
    BeneficioResponseDTO getBeneficioPorId(Integer id, Integer membresia, String documento);
    BeneficioResponseDTO updateBeneficio(Integer id, Integer membresia, String documento, BeneficioRequestDTO request);
    BeneficioResponseDTO deleteBeneficio(Integer id, Integer membresia, String documento);
}
