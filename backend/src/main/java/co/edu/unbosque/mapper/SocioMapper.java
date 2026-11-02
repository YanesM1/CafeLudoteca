package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.SocioRequestDTO;
import co.edu.unbosque.model.dto.SocioResponseDTO;
import co.edu.unbosque.model.entity.Socio;

public class SocioMapper {

    public Socio socioRequestDTOToSocio(SocioRequestDTO dto) {
        Socio socio = new Socio();
        socio.setDocumento(dto.getDocumento());
        socio.setFechaAfiliacion(dto.getFechaAfiliacion());

        return socio;
    }

    public SocioResponseDTO socioToResponseDTO(Socio socio) {
        SocioResponseDTO dto = new SocioResponseDTO();
        dto.setDocumento(socio.getDocumento());
        dto.setFechaAfiliacion(socio.getFechaAfiliacion());

        return dto;
    }
}