package co.edu.unbosque.model.dto;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoRequestDTO {
    private LocalDate fechaSalida;
    private String estadoComponentes;
    private String tipoPrestamo;
    private LocalDate fechaDevolucion;
    private String documentoCliente;
    private String documentoEmp;
    private String codigoUnidad;
    private String numSerieUnidad;
    /** Todas las unidades del préstamo (supuesto 3: uno o varios juegos físicos). */
    private java.util.List<UnidadPrestadaDTO> unidades = new java.util.ArrayList<>();
}
