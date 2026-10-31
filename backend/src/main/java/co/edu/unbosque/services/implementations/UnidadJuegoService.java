package co.edu.unbosque.services.implementations;

import java.util.Optional;

import co.edu.unbosque.mapper.UnidadJuegoMapper;
import co.edu.unbosque.model.dto.UnidadJuegoRequestDTO;
import co.edu.unbosque.model.dto.UnidadJuegoResponseDTO;
import co.edu.unbosque.model.entity.UnidadJuego;
import co.edu.unbosque.repository.implementations.UnidadJuegoRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IUnidadJuegoRepository;
import co.edu.unbosque.services.interfaces.IUnidadJuegoService;

public class UnidadJuegoService implements IUnidadJuegoService {

    private final IUnidadJuegoRepository unidadJuegoRepository =
     new UnidadJuegoRepositoryJDBC();

     private final UnidadJuegoMapper unidadJuegoMapper=new UnidadJuegoMapper();


       private boolean validarUnidadJuego(UnidadJuegoRequestDTO request){

        if(request==null){
            return false;
        }

        if(request.getCodigo()==null|| request.getCodigo().isBlank()){
            return false;
        }
        if(request.getEstado()==null|| request.getEstado().isBlank()){
            return false;
        }

        return true;
    }

    @Override
    public UnidadJuegoResponseDTO addUnidadJuego(UnidadJuegoRequestDTO request) {
       if (!validarUnidadJuego(request)) {
            return null;
        }

        UnidadJuego unidadJuego = unidadJuegoMapper.unidadJuegoRequestDTOToUnidadJuego(request);

        if (!unidadJuegoRepository.saveUnidadJuego(unidadJuego)) {
            return null;
        }

        return unidadJuegoMapper.unidadJuegoToUnidadJuegoResponseDTO(unidadJuego);
    }

    @Override
    public UnidadJuegoResponseDTO findByCodigo(String codigo) {
        Optional<UnidadJuego> unidad = unidadJuegoRepository.findByCodigo(codigo);

        if (unidad.isEmpty()) {
            return null;
        }

        return unidadJuegoMapper.unidadJuegoToUnidadJuegoResponseDTO(unidad.get());
    }

    @Override
    public UnidadJuegoResponseDTO updateUnidadJuego(String codigo, UnidadJuegoRequestDTO request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UnidadJuegoResponseDTO deleteUnidadJuego(String codigo) {
        // TODO Auto-generated method stub
        return null;
    }

}// fin class
