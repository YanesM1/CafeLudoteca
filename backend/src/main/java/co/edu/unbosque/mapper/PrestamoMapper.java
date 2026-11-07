package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.*;
import co.edu.unbosque.model.entity.Prestamo;

public class PrestamoMapper {
    public Prestamo prestamoRequestDTOToPrestamo(PrestamoRequestDTO dto) {
        Prestamo p = new Prestamo();
        p.setFechaSalida(dto.getFechaSalida());
        p.setEstadoComponentes(dto.getEstadoComponentes());
        p.setTipoPrestamo(dto.getTipoPrestamo());
        p.setFechaDevolucion(dto.getFechaDevolucion());
        p.setDocumentoCliente(dto.getDocumentoCliente());
        p.setDocumentoEmp(dto.getDocumentoEmp());
        p.setCodigoUnidad(dto.getCodigoUnidad());
        p.setNumSerieUnidad(dto.getNumSerieUnidad());
        if (dto.getUnidades() != null) p.setUnidades(new java.util.ArrayList<>(dto.getUnidades()));
        return p;
    }

    public PrestamoResponseDTO prestamoToPrestamoResponseDTO(Prestamo p) {
        PrestamoResponseDTO dto = new PrestamoResponseDTO();
        dto.setIdPrestamo(p.getIdPrestamo());
        dto.setFechaSalida(p.getFechaSalida());
        dto.setEstadoComponentes(p.getEstadoComponentes());
        dto.setTipoPrestamo(p.getTipoPrestamo());
        dto.setFechaDevolucion(p.getFechaDevolucion());
        dto.setDocumentoCliente(p.getDocumentoCliente());
        dto.setDocumentoEmp(p.getDocumentoEmp());
        dto.setCodigoUnidad(p.getCodigoUnidad());
        dto.setNumSerieUnidad(p.getNumSerieUnidad());
        if (p.getUnidades() != null) dto.setUnidades(new java.util.ArrayList<>(p.getUnidades()));
        return dto;
    }
}
