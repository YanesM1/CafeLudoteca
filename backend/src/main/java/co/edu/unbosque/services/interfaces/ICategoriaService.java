package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.CategoriaRequestDTO;
import co.edu.unbosque.model.dto.CategoriaResponseDTO;

public interface ICategoriaService {
    CategoriaResponseDTO addCategoria(CategoriaRequestDTO request);

    List<CategoriaResponseDTO> getCategoria(String categoria);

    CategoriaResponseDTO getCategoriaNumSerie(Integer numCategoria, String numSerie);

    CategoriaResponseDTO updateCategoria(Integer numCategoria, String numSerie, CategoriaRequestDTO request);

    CategoriaResponseDTO deleteCategoria(Integer numCategoria, String numSerie);

}
