package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Membresia;

public interface IMembresiaRepository {

    boolean saveMembresia(Membresia membresia);

    List<Membresia> findByDocumento(String documento);

    List<Membresia> findByEstado(String estado);

    Optional<Membresia> findByIdMembresia(String idMembresia);

    boolean updateMembresia(String idMembresia, Membresia membresia);

    boolean deleteMembresia(String idMembresia);

}