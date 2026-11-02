package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.ClienteFrecuenteRequestDTO;
import co.edu.unbosque.model.dto.ClienteFrecuenteResponseDTO;

public interface IClienteFrecuenteService {

    ClienteFrecuenteResponseDTO addClienteFrecuente(ClienteFrecuenteRequestDTO request);

    List<ClienteFrecuenteResponseDTO> getClientesFrecuentesPorNivel(String nivelFidelidad);

    ClienteFrecuenteResponseDTO getClienteFrecuentePorDocumento(String documento);

    ClienteFrecuenteResponseDTO updateClienteFrecuente(
            String documento,
            ClienteFrecuenteRequestDTO request
    );

    ClienteFrecuenteResponseDTO deleteClienteFrecuente(String documento);

}