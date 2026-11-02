package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;
import co.edu.unbosque.model.entity.Expansion;

public interface IExpansionRepository {
    boolean saveExpansion(Expansion expansion);

    List<Expansion> findByNombre(String nombre);

    Optional<Expansion> findByIdExpansion(Integer idExpansion);

    boolean updateExpansion(Integer idExpansion, Expansion expansion);

    boolean deleteExpansion(Integer idExpansion);

}
