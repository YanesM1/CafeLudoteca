package co.edu.unbosque.services.interfaces;

import java.time.LocalDate;
import java.util.List;

import co.edu.unbosque.model.dto.SocioRequestDTO;
import co.edu.unbosque.model.dto.SocioResponseDTO;

public interface ISocioService {

    SocioResponseDTO addSocio(SocioRequestDTO request);

    List<SocioResponseDTO> getSociosPorFechaAfiliacion(LocalDate fechaAfiliacion);

    SocioResponseDTO getSocioPorDocumento(String documento);

    SocioResponseDTO updateSocio(
            String documento,
            SocioRequestDTO request
    );

    SocioResponseDTO deleteSocio(String documento);

}