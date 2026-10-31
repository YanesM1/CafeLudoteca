package co.edu.unbosque.repository.interfaces;

import java.util.Optional;

import co.edu.unbosque.model.entity.UnidadJuego;

public interface IUnidadJuegoRepository {
    boolean saveUnidadJuego(UnidadJuego unidadJuego);
    Optional<UnidadJuego>findByCodigo(String codigo);
    boolean updateUnidadJuego(String codigo, UnidadJuego unidadJuego);
    boolean deleteUnidadJuego(String codigo, UnidadJuego unidadJuego);

}
