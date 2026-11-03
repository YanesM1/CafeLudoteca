package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;
import co.edu.unbosque.model.entity.Beneficio;

public class BeneficioMapper {

    public Beneficio beneficioRequestDTOToBeneficio(BeneficioRequestDTO dto) {
        Beneficio beneficio = new Beneficio();
        beneficio.setIdBeneficio(dto.getIdBeneficio());
        beneficio.setBeneficio(dto.getBeneficio());
        beneficio.setIdMembresia(dto.getIdMembresia());
        beneficio.setDocumentoSocio(dto.getDocumentoSocio());

        return beneficio;
    }

    public BeneficioResponseDTO beneficioToResponseDTO(Beneficio beneficio) {
        BeneficioResponseDTO dto = new BeneficioResponseDTO();
        dto.setIdBeneficio(beneficio.getIdBeneficio());
        dto.setBeneficio(beneficio.getBeneficio());
        dto.setIdMembresia(beneficio.getIdMembresia());
        dto.setDocumentoSocio(beneficio.getDocumentoSocio());

        return dto;
    }
}