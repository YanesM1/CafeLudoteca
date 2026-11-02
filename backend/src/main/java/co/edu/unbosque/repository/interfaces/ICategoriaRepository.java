package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Categoria;


public interface ICategoriaRepository {
      boolean saveCategoria(Categoria categoria);
    List<Categoria>findByCategoria(String categoria);
    Optional<Categoria> findByNumCategoria(Integer numCategoria, String numSerie);
    boolean updateCategoria(Integer numCategoria, String numSerie, Categoria categoria);
    boolean deleteCategoria(Integer numCategoria, String numSerie);

}
