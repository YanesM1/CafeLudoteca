package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.ElementoJuegoRequestDTO;
import co.edu.unbosque.model.dto.ElementoJuegoResponseDTO;
import co.edu.unbosque.model.entity.ElementoJuego;

public class ElementoJuegoMapper {
    public ElementoJuego DTOToElementoJuego(ElementoJuegoRequestDTO dto){
        ElementoJuego elementoJuego=new ElementoJuego();
        elementoJuego.setIdElementoJuego(dto.getIdElementoJuego());
        elementoJuego.setElementoJuego(dto.getElementoJuego());
        return elementoJuego;

    }

    public ElementoJuegoResponseDTO elementoJuegoToDTO(ElementoJuego elementoJuego){
        ElementoJuegoResponseDTO dto=new ElementoJuegoResponseDTO();
        dto.setIdElementoJuego(elementoJuego.getIdElementoJuego());
        dto.setElementoJuego(elementoJuego.getElementoJuego());
        return dto;
    }

}
