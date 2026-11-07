package co.edu.unbosque.repository.interfaces;
import java.util.Optional;

import co.edu.unbosque.model.entity.ElementoJuego;


public interface IElementoJuegoRepository {
    boolean saveElementoJuego(ElementoJuego elementoJuego);
    java.util.List<ElementoJuego> findAll(String numSerie);

    Optional<ElementoJuego> findByIdElementoJuego(Integer idElementoJuego);

    boolean updateElementoJuego(Integer idElementoJuego, ElementoJuego elementoJuego);

    boolean deleteElementoJuego(Integer idElementoJuego);

}
