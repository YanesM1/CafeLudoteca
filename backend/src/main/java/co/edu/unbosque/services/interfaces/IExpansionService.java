package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.ExpansionRequestDTO;
import co.edu.unbosque.model.dto.ExpansionResponseDTO;


public interface IExpansionService {
    ExpansionResponseDTO addExpansion(ExpansionRequestDTO request);

    List<ExpansionResponseDTO> getExpansion(String nombre);

    ExpansionResponseDTO getExpansionPorIdExpansion(Integer idExpansion);

    ExpansionResponseDTO updateExpansion(Integer idExpansion, ExpansionRequestDTO request);

    ExpansionResponseDTO deleteExpansion(Integer idExpansion);

}
