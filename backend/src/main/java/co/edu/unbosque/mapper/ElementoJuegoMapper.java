package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.ElementoJuegoRequestDTO;
import co.edu.unbosque.model.dto.ElementoJuegoResponseDTO;
import co.edu.unbosque.model.entity.ElementoJuego;

public class ElementoJuegoMapper {
    public ElementoJuego elementoJuegoRequestDTOToElementoJuego(ElementoJuegoRequestDTO dto){
        ElementoJuego elementoJuego=new ElementoJuego();
        elementoJuego.setIdElementoJuego(dto.getIdElementoJuego());
        elementoJuego.setElementoJuego(dto.getElementoJuego());
        elementoJuego.setNumSerie(dto.getNumSerie());
        return elementoJuego;

    }

    public ElementoJuegoResponseDTO elementoJuegoToElementoJuegoResponseDTO(ElementoJuego elementoJuego){
        ElementoJuegoResponseDTO dto=new ElementoJuegoResponseDTO();
        dto.setIdElementoJuego(elementoJuego.getIdElementoJuego());
        dto.setElementoJuego(elementoJuego.getElementoJuego());
        dto.setNumSerie(elementoJuego.getNumSerie());
        return dto;
    }

}
