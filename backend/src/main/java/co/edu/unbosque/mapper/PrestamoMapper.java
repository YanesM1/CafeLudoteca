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
        prestamo.setDocumentoCliente(dto.getDocumentoCliente());
        prestamo.setDocumentoEmp(dto.getDocumentoEmp());
        
        return prestamo;
    }

    public PrestamoResponseDTO prestamoToPrestamoResponseDTO(Prestamo prestamo){
        PrestamoResponseDTO dto=new PrestamoResponseDTO();
        dto.setIdPrestamo(prestamo.getIdPrestamo());
        dto.setFechaSalida(prestamo.getFechaSalida());
        dto.setEstadoComponentes(prestamo.getEstadoComponentes());
        dto.setTipoPrestamo(prestamo.getTipoPrestamo());
        dto.setFechaDevolucion(prestamo.getFechaDevolucion());
        dto.setDocumentoCliente(prestamo.getDocumentoCliente());
        dto.setDocumentoEmp(prestamo.getDocumentoEmp());
       
        return dto;

    }
}
