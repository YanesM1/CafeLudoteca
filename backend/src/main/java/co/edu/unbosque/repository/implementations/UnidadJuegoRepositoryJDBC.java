package co.edu.unbosque.repository.implementations;

import java.util.Optional;

import co.edu.unbosque.model.entity.UnidadJuego;
import co.edu.unbosque.repository.interfaces.IUnidadJuegoRepository;

public class UnidadJuegoRepositoryJDBC implements IUnidadJuegoRepository{
    @Override
    public boolean saveUnidadJuego(UnidadJuego unidadJuego) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<UnidadJuego> findByCodigo(String codigo) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }
    @Override
    public boolean updateUnidadJuego(String codigo, UnidadJuego unidadJuego) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteUnidadJuego(String codigo, UnidadJuego unidadJuego) {
        // TODO Auto-generated method stub
        return false;
    }

}
