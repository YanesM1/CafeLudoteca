package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Telefono;

public interface ITelefonoRepository {

    boolean saveTelefono(Telefono telefono);

    List<Telefono> findByDocumento(String documentoCliente);

    Optional<Telefono> findByDocumentoAndNumero(String documentoCliente, String telefono);

    boolean updateTelefono(String documentoCliente, String telefonoActual, Telefono telefono);

    boolean deleteTelefono(String documentoCliente, String telefono);
}
