package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;
import co.edu.unbosque.model.entity.Beneficio;

public class BeneficioMapper {

    public Beneficio beneficioRequestDTOToBeneficio(BeneficioRequestDTO dto) {
        Beneficio beneficio = new Beneficio();
        beneficio.setIdMembresia(dto.getIdMembresia());
        beneficio.setIdBeneficio(dto.getIdBeneficio());
        beneficio.setBeneficio(dto.getBeneficio());

        return beneficio;
    }

    public BeneficioResponseDTO beneficioToResponseDTO(Beneficio beneficio) {
        BeneficioResponseDTO dto = new BeneficioResponseDTO();
        dto.setIdMembresia(beneficio.getIdMembresia());
        dto.setIdBeneficio(beneficio.getIdBeneficio());
        dto.setBeneficio(beneficio.getBeneficio());

        return dto;
    }
}