package co.edu.unbosque.model.dto;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoResponseDTO {
    private Integer idPrestamo;
    private LocalDate fechaSalida;
    private String estadoComponentes;
    private String tipoPrestamo;
    private LocalDate fechaDevolucion;
    private String documentoCliente;
    private String documentoEmp;
    private String codigoUnidad;
    private String numSerieUnidad;
}
