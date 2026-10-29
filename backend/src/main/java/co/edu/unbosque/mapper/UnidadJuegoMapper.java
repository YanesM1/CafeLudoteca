package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.UnidadJuegoRequestDTO;
import co.edu.unbosque.model.entity.UnidadJuego;

public class UnidadJuegoMapper {

    public UnidadJuego UnidadJuegoRequestDTOToUnidadJuego(UnidadJuegoRequestDTO dto){
        UnidadJuego unidadJuego=new UnidadJuego();

        unidadJuego.setCodigo(dto.getCodigo());
        unidadJuego.setEstado(dto.getEstado());
        unidadJuego.setObservaciones(dto.getObservaciones());

        return unidadJuego;
    }

    public UnidadJuegoRequestDTO UnidadJuegoToDTO(UnidadJuego unidadJuego){
        UnidadJuegoRequestDTO dto=new UnidadJuegoRequestDTO();
        dto.setCodigo(unidadJuego.getCodigo());
        dto.setEstado(unidadJuego.getEstado());
        dto.setObservaciones(unidadJuego.getObservaciones());
        return dto;
    }


}
