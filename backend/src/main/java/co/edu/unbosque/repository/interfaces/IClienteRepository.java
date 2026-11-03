package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Cliente;

public interface IClienteRepository {

    boolean saveCliente(Cliente cliente);

    List<Cliente> findByNombre(String nombre);

    Optional<Cliente> findByDocumento(String documentoCliente);

    boolean updateCliente(String documentoCliente, Cliente cliente);

    boolean deleteCliente(String documentoCliente);

}