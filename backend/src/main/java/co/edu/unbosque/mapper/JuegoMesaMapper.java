package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.JuegoMesaRequestDTO;
import co.edu.unbosque.model.dto.JuegoMesaResponseDTO;
import co.edu.unbosque.model.entity.JuegoMesa;

public class JuegoMesaMapper {
    public JuegoMesa juegoMesaRequestDTOToJuegoMesa(JuegoMesaRequestDTO dto){
        JuegoMesa juegoMesa=new JuegoMesa();
        juegoMesa.setNumSerie(dto.getNumSerie());
        juegoMesa.setReglamento(dto.getReglamento());
        juegoMesa.setCantidadMinimaJugador(dto.getCantidadMinimaJugador());
        juegoMesa.setCantidadMaximaJugador(dto.getCantidadMaximaJugador());
        juegoMesa.setEdadMinima(dto.getEdadMinima());
        juegoMesa.setDuracionPartida(dto.getDuracionPartida());

        return juegoMesa;
    }

    public JuegoMesaResponseDTO juegoMesaToResponseDTO(JuegoMesa juegoMesa){
        JuegoMesaResponseDTO dto=new JuegoMesaResponseDTO();
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
