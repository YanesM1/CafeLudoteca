package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.ClienteRequestDTO;
import co.edu.unbosque.model.dto.ClienteResponseDTO;

public interface IClienteService {

    ClienteResponseDTO addCliente(ClienteRequestDTO request);

    List<ClienteResponseDTO> getCliente(String nombre);

    ClienteResponseDTO getClientePorDocumento(String documento);

    ClienteResponseDTO updateCliente(
            String documento,
            ClienteRequestDTO request
    );

    ClienteResponseDTO deleteCliente(String documento);

}