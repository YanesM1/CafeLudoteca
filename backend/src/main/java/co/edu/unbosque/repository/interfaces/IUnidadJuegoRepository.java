package co.edu.unbosque.repository.interfaces;
import java.util.Optional;
import co.edu.unbosque.model.entity.UnidadJuego;

public interface IUnidadJuegoRepository{
    boolean saveUnidadJuego(UnidadJuego unidadJuego);
    Optional<UnidadJuego> findByCodigo(String codigo, String numSerie);
    boolean updateUnidadJuego(String codigo, String numSerie, UnidadJuego unidadJuego);
    boolean deleteUnidadJuego(String codigo, String numSerie);
}