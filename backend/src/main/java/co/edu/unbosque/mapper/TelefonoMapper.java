package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.TelefonoRequestDTO;
import co.edu.unbosque.model.dto.TelefonoResponseDTO;
import co.edu.unbosque.model.entity.Telefono;

public class TelefonoMapper {

    public Telefono telefonoRequestDTOToTelefono(TelefonoRequestDTO dto) {
        Telefono telefono = new Telefono();
        telefono.setDocumentoCliente(dto.getDocumentoCliente());
        telefono.setTelefono(dto.getTelefono());

        return telefono;
    }

    public TelefonoResponseDTO telefonoToResponseDTO(Telefono telefono) {
        TelefonoResponseDTO dto = new TelefonoResponseDTO();
        dto.setDocumentoCliente(telefono.getDocumentoCliente());
        dto.setTelefono(telefono.getTelefono());;

        return dto;
    }
}