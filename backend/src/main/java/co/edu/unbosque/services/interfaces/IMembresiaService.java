package co.edu.unbosque.services.interfaces;

import java.util.List;

import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import co.edu.unbosque.model.dto.MembresiaResponseDTO;

public interface IMembresiaService {
    MembresiaResponseDTO addMembresia(MembresiaRequestDTO request);

    MembresiaResponseDTO getMembresiaPorId(int idMembresia);

    List<MembresiaResponseDTO> getMembresiaPorDocumento(String documento);

    List<MembresiaResponseDTO> getMembresiaPorEstado(String estado);

    MembresiaResponseDTO updateMembresia(int idMembresia, MembresiaRequestDTO request);

    MembresiaResponseDTO deleteMembresia(int idMembresia);
}
