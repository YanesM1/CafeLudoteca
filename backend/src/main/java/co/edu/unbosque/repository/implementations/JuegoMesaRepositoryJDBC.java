package co.edu.unbosque.repository.implementations;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.JuegoMesa;
import co.edu.unbosque.repository.interfaces.IJuegoMesaRepository;

public class JuegoMesaRepositoryJDBC implements IJuegoMesaRepository{

    @Override
    public List<JuegoMesa> findByNombre(String nombre) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<JuegoMesa> findByNumSerie(String numSerie) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }
    @Override
    public boolean saveJuegoMesa(JuegoMesa juegoMesa) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean updateJuegoMesa(String numSerie, JuegoMesa juegoMesa) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean deleteJuegoMesa(String numSerie) {
        // TODO Auto-generated method stub
        return false;
    }

}