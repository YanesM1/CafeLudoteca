package co.edu.unbosque.repository.interfaces;

import co.edu.unbosque.model.dto.DisponibilidadUnidadJuegoDTO;
import java.util.List;import java.util.Optional;
import co.edu.unbosque.model.entity.UnidadJuego;

public interface IUnidadJuegoRepository{
    boolean saveUnidadJuego(UnidadJuego unidadJuego);
    Optional<UnidadJuego> findByCodigo(String codigo, String numSerie);
    boolean updateUnidadJuego(String codigo, String numSerie, UnidadJuego unidadJuego);
    boolean deleteUnidadJuego(String codigo, String numSerie);

    List<DisponibilidadUnidadJuegoDTO> consultarDisponibilidad();
    List<UnidadJuego> findDisponibles();
}