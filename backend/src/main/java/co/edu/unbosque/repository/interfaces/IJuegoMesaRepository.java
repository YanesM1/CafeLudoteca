package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.JuegoMesa;

public interface IJuegoMesaRepository {
    boolean saveJuegoMesa(JuegoMesa juegoMesa);
    List<JuegoMesa>findByNombre(String nombre);
    Optional<JuegoMesa> findByNumSerie(String numSerie);
    boolean updateJuegoMesa(String numSerie, JuegoMesa juegoMesa);
    boolean deleteJuegoMesa(String numSerie);

}
