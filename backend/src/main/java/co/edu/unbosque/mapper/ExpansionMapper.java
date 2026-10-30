package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.ExpansionRequestDTO;
import co.edu.unbosque.model.dto.ExpansionResponseDTO;
import co.edu.unbosque.model.entity.Expansion;

public class ExpansionMapper{

    public Expansion expansionRequestDTOToExpansion(ExpansionRequestDTO dto){
        Expansion expansion=new Expansion();
        expansion.setIdExpansion(dto.getIdExpansion());
        expansion.setNombre(dto.getNombre());
        expansion.setDescripcion(dto.getDescripcion());
        expansion.setContenidoAdicional(dto.getContenidoAdicional());
        return expansion;
        
    }

    public ExpansionResponseDTO expansionTodto(Expansion expansion){
        ExpansionResponseDTO dto=new ExpansionResponseDTO();
        dto.setIdExpansion(expansion.getIdExpansion());
        dto.setNombre(expansion.getNombre());
        dto.setDescripcion(expansion.getDescripcion());
        dto.setContenidoAdicional(expansion.getDescripcion());
        return dto;
    }
}
