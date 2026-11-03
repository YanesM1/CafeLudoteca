package co.edu.unbosque.services.interfaces;

import java.util.List;
import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import co.edu.unbosque.model.dto.MembresiaResponseDTO;

public interface IMembresiaService {
    MembresiaResponseDTO addMembresia(MembresiaRequestDTO request);
    List<MembresiaResponseDTO> getTodas();
    MembresiaResponseDTO getMembresiaPorId(int id, String documento);
    List<MembresiaResponseDTO> getMembresiaPorDocumento(String documento);
    List<MembresiaResponseDTO> getMembresiaPorEstado(String estado);
    MembresiaResponseDTO updateMembresia(int id, String documento, MembresiaRequestDTO request);
    MembresiaResponseDTO deleteMembresia(int id, String documento);
}
