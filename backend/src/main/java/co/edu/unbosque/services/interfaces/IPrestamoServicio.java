package co.edu.unbosque.services.interfaces;

import java.util.List;


import co.edu.unbosque.model.dto.PrestamoRequestDTO;
import co.edu.unbosque.model.dto.PrestamoResponseDTO;

public interface IPrestamoServicio {
     PrestamoResponseDTO addPrestamo(PrestamoRequestDTO request);

    List<PrestamoResponseDTO> getPrestamo(String tipoPrestamo);

    PrestamoResponseDTO getPrestamoByIdPrestamo(Integer idPrestamo);

    PrestamoResponseDTO updatePrestamo(Integer idPrestamo, PrestamoRequestDTO request);

    PrestamoResponseDTO deletePrestamo(Integer idPrestamo);

}
