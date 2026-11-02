package co.edu.unbosque.controller.interfaces;

import co.edu.unbosque.model.dto.CategoriaRequestDTO;
import jakarta.ws.rs.core.Response;

public interface ICategoriaController {
    Response guardarCategoria(CategoriaRequestDTO request);
    Response buscarPorCategoria(String categoria);
    Response buscarPorNumCategoria(Integer numCategoria, String numSerie);
    Response actualizarCategoria(Integer numCategoria, String numSerie, CategoriaRequestDTO request);
    Response borrarCategoria(Integer numCategoria, String numSerie);

}
