
package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.MembresiaMapper;
import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import co.edu.unbosque.model.dto.MembresiaResponseDTO;
import co.edu.unbosque.model.entity.Membresia;
import co.edu.unbosque.repository.implementations.MembresiaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IMembresiaRepository;
import co.edu.unbosque.services.interfaces.IMembresiaService;

public class MembresiaService implements IMembresiaService {

    private final IMembresiaRepository membresiaRepository =new MembresiaRepositoryJDBC();
    private final MembresiaMapper membresiaMapper =new MembresiaMapper();


    private boolean validarMembresia(MembresiaRequestDTO request) {

        if (request == null) {
            return false;
        }

        if (request.getPlan() == null || request.getPlan().isBlank()) {
            return false;
        }

        if (request.getEstado() == null || request.getEstado().isBlank()) {
            return false;
        }

        if (request.getFechaInicio() == null) {
            return false;
        }

        if (request.getFechaFin() == null) {
            return false;
        }

        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            return false;
        }

        if (request.getTarifa() <= 0) {
            return false;
        }

        return true;
    }

 
    @Override
    public MembresiaResponseDTO addMembresia(MembresiaRequestDTO request) {

        if (!validarMembresia(request)) {
            return null;
        }

        Membresia membresia =membresiaMapper.membresiaRequestDTOToMembresia(request);

        if (!membresiaRepository.saveMembresia(membresia)) {
            return null;
        }

        return membresiaMapper.membresiaToMembresiaResponseDTO(membresia);
    }


    @Override
    public MembresiaResponseDTO getMembresiaPorId(int idMembresia) {

        if (idMembresia <= 0) {
            return null;
        }

        Optional<Membresia> membresia =
                membresiaRepository.findByIdMembresia(idMembresia);

        if (membresia.isEmpty()) {
            return null;
        }

        return membresiaMapper.membresiaToMembresiaResponseDTO(membresia.get());
    }


    @Override
    public MembresiaResponseDTO updateMembresia(int idMembresia, MembresiaRequestDTO request) {

        if (idMembresia <= 0 || !validarMembresia(request)) {
            return null;
        }

        Optional<Membresia> existente =membresiaRepository.findByIdMembresia(idMembresia);

        if (existente.isEmpty()) {
            return null;
        }

        Membresia membresia = membresiaMapper.membresiaRequestDTOToMembresia(request);

        if (!membresiaRepository.updateMembresia(idMembresia, membresia)) {
            return null;
        }

        Optional<Membresia> actualizada =membresiaRepository.findByIdMembresia(idMembresia);

        if (actualizada.isEmpty()) {
            return null;
        }

        return membresiaMapper.membresiaToMembresiaResponseDTO(
                actualizada.get()
        );
    }



    @Override
    public MembresiaResponseDTO deleteMembresia(int idMembresia) {
        if (idMembresia <= 0) {
            return null;
        }

        Optional<Membresia> membresia =membresiaRepository.findByIdMembresia(idMembresia);

        if (membresia.isEmpty()) {
            return null;
        }

        if (!membresiaRepository.deleteMembresia(idMembresia)) {
            return null;
        }

        return membresiaMapper.membresiaToMembresiaResponseDTO(membresia.get());
      
    }
  
   

    @Override
    public List<MembresiaResponseDTO> getMembresiaPorDocumento(String documento) {

        List<MembresiaResponseDTO> response = new ArrayList<>();

        if (documento == null || documento.isBlank()) {
            return response;
        }

        List<Membresia> membresias =
                membresiaRepository.findByDocumento(documento.trim());

        for (Membresia membresia : membresias) {
            response.add(
                    membresiaMapper.membresiaToMembresiaResponseDTO(membresia));
        }

        return response;
    }
@Override
public List<MembresiaResponseDTO> getMembresiaPorEstado(String estado) {
     List<MembresiaResponseDTO> response = new ArrayList<>();

    if (estado == null || estado.isBlank()) {
        return response;
    }

    List<Membresia> membresias =
            membresiaRepository.findByEstado(estado.trim());

    for (Membresia membresia : membresias) {
        response.add(
                membresiaMapper.membresiaToMembresiaResponseDTO(membresia));
    }

    return response;
 
}


   
}
