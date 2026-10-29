package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.JuegoMesaRequestDTO;
import co.edu.unbosque.model.entity.JuegoMesa;

public class JuegoMesaMapper {
    public JuegoMesa JuegoMesaRequestDTOToJuegoMesa(JuegoMesaRequestDTO dto){
        JuegoMesa juegoMesa=new JuegoMesa();
        juegoMesa.setNumSerie(dto.getNumSerie());
        juegoMesa.setReglamento(dto.getReglamento());
        juegoMesa.setCantidadMinimaJugador(dto.getCantidadMinimaJugador());
        juegoMesa.setCantidadMaximaJugador(dto.getCantidadMaximaJugador());
        juegoMesa.setEdadMinima(dto.getEdadMinima());
        juegoMesa.setDuracionPartida(dto.getDuracionPartida());

        return juegoMesa;
    }

    public JuegoMesaRequestDTO JuegoMesaRequestDTO(JuegoMesa juegoMesa){
        JuegoMesaRequestDTO dto=new JuegoMesaRequestDTO();
        dto.setNumSerie(juegoMesa.getNumSerie());
        dto.setReglamento(juegoMesa.getReglamento());
        dto.setCantidadMinimaJugador(juegoMesa.getCantidadMinimaJugador());
        dto.setCantidadMaximaJugador(juegoMesa.getCantidadMaximaJugador());
        dto.setEdadMinima(juegoMesa.getEdadMinima());
        dto.setNombre(juegoMesa.getNombre());
        dto.setDuracionPartida(juegoMesa.getDuracionPartida());
        return dto;
        
    }
}
