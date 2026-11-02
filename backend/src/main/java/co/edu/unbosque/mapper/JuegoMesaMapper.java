package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.JuegoMesaRequestDTO;
import co.edu.unbosque.model.dto.JuegoMesaResponseDTO;
import co.edu.unbosque.model.entity.JuegoMesa;

public class JuegoMesaMapper {
    public JuegoMesa juegoMesaRequestDTOToJuegoMesa(JuegoMesaRequestDTO dto){
        JuegoMesa juegoMesa=new JuegoMesa();
        juegoMesa.setNumSerie(dto.getNumSerie());
        juegoMesa.setReglamento(dto.getReglamento());
        juegoMesa.setCantidadMinimaJugadores(dto.getCantidadMinimaJugadores());
        juegoMesa.setCantidadMaximaJugadores(dto.getCantidadMaximaJugadores());
        juegoMesa.setEdadMinima(dto.getEdadMinima());
        juegoMesa.setNombre(dto.getNombre());
        juegoMesa.setDuracionPartida(dto.getDuracionPartida());

        return juegoMesa;
    }

    public JuegoMesaResponseDTO juegoMesaToResponseDTO(JuegoMesa juegoMesa){
        JuegoMesaResponseDTO dto=new JuegoMesaResponseDTO();
        dto.setNumSerie(juegoMesa.getNumSerie());
        dto.setReglamento(juegoMesa.getReglamento());
        dto.setCantidadMinimaJugadores(juegoMesa.getCantidadMinimaJugadores());
        dto.setCantidadMaximaJugadores(juegoMesa.getCantidadMaximaJugadores());
        dto.setEdadMinima(juegoMesa.getEdadMinima());
        dto.setNombre(juegoMesa.getNombre());
        dto.setDuracionPartida(juegoMesa.getDuracionPartida());
        return dto;
        
    }
}
