package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Membresia;

public interface IMembresiaRepository {
    boolean saveMembresia(Membresia membresia);
    List<Membresia> findByEstado(String estado);
    Optional<Membresia> findByIdMembresia(int idMembresia);
    List<Membresia> findByDocumento(String documento);
    boolean updateMembresia(int idMembresia, Membresia membresia);
    boolean deleteMembresia(int idMembresia);
}