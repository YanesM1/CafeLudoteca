package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Telefono;

public interface ITelefonoRepository {

    boolean saveTelefono(Telefono telefono);

    List<Telefono> findByDocumento(String documento);

    Optional<Telefono> findByDocumentoAndNumero(String documento, String numero);

    boolean updateTelefono(String documento, String numeroActual, Telefono telefono);

    boolean deleteTelefono(String documento, String numero);

}