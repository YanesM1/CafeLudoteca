package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.JuegoMesaRequestDTO;
import co.edu.unbosque.model.dto.JuegoMesaResponseDTO;

public interface IJuegoMesaService {
    JuegoMesaResponseDTO addJuegoMesa(JuegoMesaRequestDTO request);
    List<JuegoMesaResponseDTO>getJuegoMesa(String nombre);
    JuegoMesaResponseDTO getJuegosPorNumSerie(String numSerie);
    JuegoMesaResponseDTO updateJuegoMesa(String numSerie, JuegoMesaRequestDTO request);
    JuegoMesaResponseDTO deleteJuegoMesa(String numSerie);
    
}
