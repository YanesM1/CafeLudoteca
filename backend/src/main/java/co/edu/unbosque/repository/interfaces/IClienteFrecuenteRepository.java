package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.ClienteFrecuente;

public interface IClienteFrecuenteRepository {

    boolean saveClienteFrecuente(ClienteFrecuente clienteFrecuente);

    List<ClienteFrecuente> findByNivelFidelidad(String nivelFidelidad);

    Optional<ClienteFrecuente> findByDocumento(String documento);

    boolean updateClienteFrecuente(String documento, ClienteFrecuente clienteFrecuente);

    boolean deleteClienteFrecuente(String documento);

}