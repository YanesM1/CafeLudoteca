package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.ClienteFrecuenteRequestDTO;
import co.edu.unbosque.model.dto.ClienteFrecuenteResponseDTO;
import co.edu.unbosque.model.entity.ClienteFrecuente;

public class ClienteFrecuenteMapper {

    public ClienteFrecuente clienteFrecuenteRequestDTOToClienteFrecuente(
            ClienteFrecuenteRequestDTO dto) {
        ClienteFrecuente clienteFrecuente = new ClienteFrecuente();
        clienteFrecuente.setDocumentoCliente(dto.getDocumentoCliente());
        clienteFrecuente.setPuntosAcumulados(dto.getPuntosAcumulados());
        clienteFrecuente.setNivelFidelidad(dto.getNivelFidelidad());

        return clienteFrecuente;
    }

    public ClienteFrecuenteResponseDTO clienteFrecuenteToResponseDTO(
            ClienteFrecuente clienteFrecuente) {
        ClienteFrecuenteResponseDTO dto = new ClienteFrecuenteResponseDTO();
        dto.setDocumentoCliente(clienteFrecuente.getDocumentoCliente());
        dto.setPuntosAcumulados(clienteFrecuente.getPuntosAcumulados());
        dto.setNivelFidelidad(clienteFrecuente.getNivelFidelidad());

        return dto;
    }
}