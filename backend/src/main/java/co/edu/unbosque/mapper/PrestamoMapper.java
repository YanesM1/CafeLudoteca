package co.edu.unbosque.mapper;
import co.edu.unbosque.model.dto.PrestamoRequestDTO;
import co.edu.unbosque.model.dto.PrestamoResponseDTO;
import co.edu.unbosque.model.entity.Prestamo;

public class PrestamoMapper {
    public Prestamo prestamoRequestDTOToPrestamo(PrestamoRequestDTO dto){
        Prestamo prestamo=new Prestamo();
        prestamo.setFechaSalida(dto.getFechaSalida());
        prestamo.setEstadoComponentes(dto.getEstadoComponentes());
        prestamo.setTipoPrestamo(dto.getTipoPrestamo());
        prestamo.setFechaDevolucion(dto.getFechaDevolucion());
        prestamo.setClienteDocumento(dto.getClienteDocumento());
        prestamo.setEmpleadoDocumento(dto.getEmpleadoDocumento());
        
        return prestamo;
    }

    public PrestamoResponseDTO prestamoToPrestamoResponseDTO(Prestamo prestamo){
        PrestamoResponseDTO dto=new PrestamoResponseDTO();
        dto.setIdPrestamo(prestamo.getIdPrestamo());
        dto.setFechaSalida(prestamo.getFechaSalida());
        dto.setEstadoComponentes(prestamo.getEstadoComponentes());
        dto.setTipoPrestamo(prestamo.getTipoPrestamo());
        dto.setFechaDevolucion(prestamo.getFechaDevolucion());
        dto.setClienteDocumento(prestamo.getClienteDocumento());
        dto.setEmpleadoDocumento(prestamo.getEmpleadoDocumento());
       
        return dto;

    }
}
