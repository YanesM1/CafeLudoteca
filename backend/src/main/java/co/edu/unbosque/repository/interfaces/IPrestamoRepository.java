package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;
import co.edu.unbosque.model.entity.Prestamo;
import co.edu.unbosque.model.dto.DevolucionPrestamoRequestDTO;

public interface IPrestamoRepository {
    boolean savePrestamo(Prestamo prestamo);
    List<Prestamo> findAll();
    List<Prestamo> findActivos();
    List<Prestamo> findByTipoPrestamo(String tipoPrestamo);
    Optional<Prestamo> findByIdPrestamo(Integer idPrestamo);
    boolean updatePrestamo(Integer idPrestamo, Prestamo prestamo);
    boolean registrarDevolucion(Integer idPrestamo, DevolucionPrestamoRequestDTO request);
    boolean deletePrestamo(Integer idPrestamo);
}
