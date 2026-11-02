package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;

public interface IBeneficioService {

    BeneficioResponseDTO addBeneficio(BeneficioRequestDTO request);

    List<BeneficioResponseDTO> getBeneficios();

    BeneficioResponseDTO getBeneficioPorId(Integer idBeneficio, Integer idMembresia);

    BeneficioResponseDTO updateBeneficio(Integer idBeneficio, Integer idMembresia, BeneficioRequestDTO request);

    BeneficioResponseDTO deleteBeneficio(Integer idBeneficio, Integer idMembresia);
}