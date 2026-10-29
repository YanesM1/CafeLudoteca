
package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Mesa;

public interface IMesaRepository {

    boolean saveMesa(Mesa mesa);

    List<Mesa> findByTematica(String tematica);

    Optional<Mesa> findByIdMesa(String idMesa);

    boolean updateMesa(String idMesa, Mesa mesa);

    boolean deleteMesa(String idMesa);

}
