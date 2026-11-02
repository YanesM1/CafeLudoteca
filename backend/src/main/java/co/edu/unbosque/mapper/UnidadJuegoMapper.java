
package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.UnidadJuegoRequestDTO;
import co.edu.unbosque.model.dto.UnidadJuegoResponseDTO;
import co.edu.unbosque.model.entity.UnidadJuego;

public class UnidadJuegoMapper {

    public UnidadJuego unidadJuegoRequestDTOToUnidadJuego(UnidadJuegoRequestDTO dto){
        UnidadJuego unidadJuego=new UnidadJuego();

        unidadJuego.setCodigo(dto.getCodigo());
        unidadJuego.setEstado(dto.getEstado());
        unidadJuego.setObservaciones(dto.getObservaciones());
        unidadJuego.setNumSerie(dto.getNumSerie());

        return unidadJuego;
    }

    public UnidadJuegoResponseDTO unidadJuegoToUnidadJuegoResponseDTO(UnidadJuego unidadJuego){
        UnidadJuegoResponseDTO dto=new UnidadJuegoResponseDTO();
        dto.setCodigo(unidadJuego.getCodigo());
        dto.setEstado(unidadJuego.getEstado());
        dto.setObservaciones(unidadJuego.getObservaciones());
        dto.setNumSerie(unidadJuego.getNumSerie());
        return dto;
    }


}
