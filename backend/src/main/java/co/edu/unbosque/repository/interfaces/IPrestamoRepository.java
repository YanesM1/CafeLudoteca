package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;
import co.edu.unbosque.model.entity.Prestamo;

public interface IPrestamoRepository {
     boolean savePrestamo(Prestamo prestamo);

    List<Prestamo> findByTipoPrestamo(String tipoPrestamo);

    Optional<Prestamo> findByIdPrestamo(Integer idPrestamo);

    boolean updatePrestamo(Integer idPrestamo, Prestamo prestamo);

    boolean deletePrestamo(Integer idPrestamo);

}
