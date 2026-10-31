package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.TelefonoRequestDTO;
import co.edu.unbosque.model.dto.TelefonoResponseDTO;
import co.edu.unbosque.model.entity.Telefono;

public class TelefonoMapper {

    public Telefono telefonoRequestDTOToTelefono(TelefonoRequestDTO dto) {
        Telefono telefono = new Telefono();
        telefono.setDocumento(dto.getDocumento());
        telefono.setNumero(dto.getNumero());

        return telefono;
    }

    public TelefonoResponseDTO telefonoToResponseDTO(Telefono telefono) {
        TelefonoResponseDTO dto = new TelefonoResponseDTO();
        dto.setDocumento(telefono.getDocumento());
        dto.setNumero(telefono.getNumero());

        return dto;
    }
}