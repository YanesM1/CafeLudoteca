package co.edu.unbosque.services.interfaces;

import java.util.List;
import co.edu.unbosque.model.dto.*;

public interface IPrestamoServicio {
    PrestamoResponseDTO addPrestamo(PrestamoRequestDTO request);
    List<PrestamoResponseDTO> getPrestamo(String tipoPrestamo);
    List<PrestamoResponseDTO> getActivos();
    List<PrestamoResponseDTO> getHistorial();
    PrestamoResponseDTO getPrestamoByIdPrestamo(Integer idPrestamo);
    PrestamoResponseDTO updatePrestamo(Integer idPrestamo, PrestamoRequestDTO request);
    PrestamoResponseDTO registrarDevolucion(Integer id, DevolucionPrestamoRequestDTO request);
    PrestamoResponseDTO deletePrestamo(Integer idPrestamo);
}
