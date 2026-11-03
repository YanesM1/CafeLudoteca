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
        return p;
    }

    public PrestamoResponseDTO prestamoToPrestamoResponseDTO(Prestamo p) {
        return new PrestamoResponseDTO(
            p.getIdPrestamo(), p.getFechaSalida(),
            p.getEstadoComponentes(), p.getTipoPrestamo(),
            p.getFechaDevolucion(), p.getDocumentoCliente(),
            p.getDocumentoEmp(), p.getCodigoUnidad(), p.getNumSerieUnidad()
        );
    }
}
