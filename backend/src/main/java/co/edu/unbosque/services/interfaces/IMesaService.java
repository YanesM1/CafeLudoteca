
package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.MesaRequestDTO;
import co.edu.unbosque.model.dto.MesaResponseDTO;

public interface IMesaService {

    MesaResponseDTO addMesa(MesaRequestDTO request);

    List<MesaResponseDTO> getMesa(String tematica);

    MesaResponseDTO getMesasPorIdMesa(String idMesa);

    MesaResponseDTO updateMesa(
            String idMesa,
            MesaRequestDTO request
    );

    MesaResponseDTO deleteMesa(String idMesa);

}
