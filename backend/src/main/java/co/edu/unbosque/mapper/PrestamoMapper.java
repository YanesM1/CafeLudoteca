package co.edu.unbosque.mapper;
import co.edu.unbosque.model.dto.PrestamoRequestDTO;
import co.edu.unbosque.model.dto.PrestamoResponseDTO;
import co.edu.unbosque.model.entity.Prestamo;

public class PrestamoMapper {
    public Prestamo prestamoRequestDTOToPrestamo(PrestamoRequestDTO dto){
        Prestamo prestamo=new Prestamo();
        prestamo.setIdPrestamo(dto.getIdPrestamo());
        prestamo.setFechaSalida(dto.getFechaSalida());
        prestamo.setEstadoComponentes(dto.getEstadoComponentes());
        prestamo.setTipoPrestamo(dto.getTipoPrestamo());
        prestamo.setFechaDevolucion(dto.getFechaDevolucion());
        return prestamo;
    }

    public PrestamoResponseDTO PrestamoToPrestamoResponseDTO(Prestamo prestamo){
        PrestamoResponseDTO dto=new PrestamoResponseDTO();
        dto.setIdPrestamo(prestamo.getIdPrestamo());
        dto.setFechaSalida(prestamo.getFechaSalida());
        dto.setEstadoComponentes(prestamo.getEstadoComponentes());
        dto.setTipoPrestamo(prestamo.getTipoPrestamo());
        dto.setFechaDevolucion(prestamo.getFechaDevolucion());
        return dto;

    }
}
