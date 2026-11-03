package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;
import co.edu.unbosque.model.entity.Membresia;

public interface IMembresiaRepository {
    boolean saveMembresia(Membresia m);
    List<Membresia> findAll();
    List<Membresia> findByEstado(String estado);
    List<Membresia> findByDocumento(String documento);
    Optional<Membresia> findByIdMembresia(int id, String documento);
    boolean updateMembresia(int id, String documento, Membresia m);
    boolean deleteMembresia(int id, String documento);
}
