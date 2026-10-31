package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.MembresiaRequestDTO;
import co.edu.unbosque.model.dto.MembresiaResponseDTO;
import co.edu.unbosque.model.entity.Membresia;

public class MembresiaMapper {

    public Membresia membresiaRequestDTOToMembresia(MembresiaRequestDTO dto) {
        Membresia membresia = new Membresia();
        membresia.setIdMembresia(dto.getIdMembresia());
        membresia.setPlan(dto.getPlan());
        membresia.setEstado(dto.getEstado());
        membresia.setFechaInicio(dto.getFechaInicio());
        membresia.setFechaFin(dto.getFechaFin());
        membresia.setDocumento(dto.getDocumento());
        membresia.setTarifa(dto.getTarifa());

        return membresia;
    }

    public MembresiaResponseDTO membresiaToResponseDTO(Membresia membresia) {
        MembresiaResponseDTO dto = new MembresiaResponseDTO();
        dto.setIdMembresia(membresia.getIdMembresia());
        dto.setPlan(membresia.getPlan());
        dto.setEstado(membresia.getEstado());
        dto.setFechaInicio(membresia.getFechaInicio());
        dto.setFechaFin(membresia.getFechaFin());
        dto.setDocumento(membresia.getDocumento());
        dto.setTarifa(membresia.getTarifa());

        return dto;
    }
}