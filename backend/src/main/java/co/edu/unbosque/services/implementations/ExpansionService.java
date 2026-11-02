package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.ExpansionMapper;
import co.edu.unbosque.model.dto.ExpansionRequestDTO;
import co.edu.unbosque.model.dto.ExpansionResponseDTO;
import co.edu.unbosque.model.entity.Expansion;
import co.edu.unbosque.repository.implementations.ExpansionRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IExpansionRepository;
import co.edu.unbosque.services.interfaces.IExpansionService;

public class ExpansionService implements  IExpansionService{
    private final IExpansionRepository expansionRepository = new ExpansionRepositoryJDBC();
    private final ExpansionMapper expansionMapper = new ExpansionMapper();


 private boolean validarExpansion(ExpansionRequestDTO request){

        if(request==null){
            return false;
        }

        if(request.getIdExpansion()<=0){
            return false;
        }
       
        if(request.getNombre()==null|| request.getNombre().isBlank()){
            return false;
        }
        if(request.getDescripcion()==null || request.getDescripcion().isBlank()){
            return false;
        }
    
        if(request.getContenidoAdicional()==null||request.getContenidoAdicional().isBlank()){
            return false;
        }
     
        if(request.getNumSerie()==null||request.getNumSerie().isBlank()){
            return false;
        }

        return true;
    }





    @Override
    public ExpansionResponseDTO addExpansion(ExpansionRequestDTO request) {
        if (!validarExpansion(request)) {
            return null;
        }

        Expansion expansion= expansionMapper.expansionRequestDTOToExpansion(request);

        if (!expansionRepository.saveExpansion(expansion)) {
            return null;
        }

        return expansionMapper.expansionToExpansionResponseDTO(expansion);
        }


        @Override
        public List<ExpansionResponseDTO> getExpansion(String nombre) {
            List<Expansion> expansiones = expansionRepository.findByNombre(nombre);
        List<ExpansionResponseDTO> response = new ArrayList<>();

        if(expansiones==null){
            return response;
        }

        for (Expansion expansionJ : expansiones) {
            response.add(
                    expansionMapper.expansionToExpansionResponseDTO(expansionJ));
        }

        return response;
        }


       @Override
       public ExpansionResponseDTO getExpansionPorIdExpansion(Integer idExpansion) {
            Optional<Expansion> expansion = expansionRepository.findByIdExpansion(idExpansion);

        if (expansion.isEmpty()) {
            return null;
        }

        return expansionMapper.expansionToExpansionResponseDTO(
                expansion.get());
       }



       @Override
       public ExpansionResponseDTO updateExpansion(Integer idExpansion, ExpansionRequestDTO request) {
           if (!validarExpansion(request)) {
            return null;
        }

        Expansion expansion = expansionMapper.expansionRequestDTOToExpansion(request);

        if (!expansionRepository.updateExpansion(idExpansion, expansion)) {
            return null;
        }

        Optional<Expansion> actualizado = expansionRepository.findByIdExpansion(idExpansion);

        if (actualizado.isEmpty()) {
            return null;
        }

        return expansionMapper.expansionToExpansionResponseDTO(actualizado.get());
       }



       @Override
       public ExpansionResponseDTO deleteExpansion(Integer idExpansion) {
           Optional<Expansion> expansion = expansionRepository.findByIdExpansion(idExpansion);

        if (expansion.isEmpty()) {
            return null;
        }

        if (!expansionRepository.deleteExpansion(idExpansion)) {
            return null;
        }

        return expansionMapper.expansionToExpansionResponseDTO(
                expansion.get());
       }
    }
