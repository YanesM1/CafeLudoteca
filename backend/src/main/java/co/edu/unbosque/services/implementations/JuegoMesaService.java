package co.edu.unbosque.services.implementations;

import java.util.List;

import co.edu.unbosque.mapper.JuegoMesaMapper;
import co.edu.unbosque.model.dto.JuegoMesaRequestDTO;
import co.edu.unbosque.model.dto.JuegoMesaResponseDTO;
import co.edu.unbosque.model.entity.JuegoMesa;
import co.edu.unbosque.repository.implementations.JuegoMesaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IJuegoMesaRepository;
import co.edu.unbosque.services.interfaces.IJuegoMesaService;
import jakarta.ws.rs.POST;

public class JuegoMesaService implements IJuegoMesaService{

    private final IJuegoMesaRepository juegoMesaRepository=new 
    JuegoMesaRepositoryJDBC();
    private final JuegoMesaMapper juegoMesaMapper=new
    JuegoMesaMapper();


  @Override
    public JuegoMesaResponseDTO addJuegoMesa(JuegoMesaRequestDTO request) {
        if(request==null){
            return null;
        }
        JuegoMesa juegoMesa=
        juegoMesaMapper.juegoMesaRequestDTOToJuegoMesa(request);
        if(!juegoMesaRepository.saveJuegoMesa(juegoMesa)){
            return null;
        }
        
        return null;
    }


    @Override
    public List<JuegoMesaResponseDTO> getJuegoMesa(String nombre) {
        
        return null;
    }

    @Override
    public JuegoMesaResponseDTO updateJuegoMesa(String numSerie, JuegoMesaRequestDTO request) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public JuegoMesaResponseDTO getJuegosPorNumSerie(String numSeriea) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public JuegoMesaResponseDTO deleteJuegoMesa(String numSerie) {
        // TODO Auto-generated method stub
        return null;
    }
  


}
