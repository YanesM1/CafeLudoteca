package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.TelefonoRequestDTO;
import co.edu.unbosque.model.dto.TelefonoResponseDTO;

public interface ITelefonoService {

    TelefonoResponseDTO addTelefono(TelefonoRequestDTO request);

    List<TelefonoResponseDTO> getTelefonosPorDocumento(String documento);

    TelefonoResponseDTO getTelefonoPorNumero(String documento, String numero);

    TelefonoResponseDTO updateTelefono(
            String documento,
            String numeroActual,
            TelefonoRequestDTO request
    );

    TelefonoResponseDTO deleteTelefono(String documento, String numero);

}