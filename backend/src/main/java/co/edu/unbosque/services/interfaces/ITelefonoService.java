package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.TelefonoRequestDTO;
import co.edu.unbosque.model.dto.TelefonoResponseDTO;

public interface ITelefonoService {

    TelefonoResponseDTO addTelefono(TelefonoRequestDTO request);

    List<TelefonoResponseDTO> getTelefonosPorDocumento(String documentoCliente);

    TelefonoResponseDTO updateTelefono(String documentoCliente, String telefonoActual,
            TelefonoRequestDTO request);

    TelefonoResponseDTO deleteTelefono(String documentoCliente, String telefono);
}
