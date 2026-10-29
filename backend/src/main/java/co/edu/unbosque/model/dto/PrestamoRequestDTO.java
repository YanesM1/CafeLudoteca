package co.edu.unbosque.model.dto;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoRequestDTO {
    private int idPrestamo;
    private LocalDate fechaSalida;
    private String estadoComponentes;
    private String tipoPrestamo;
    private LocalDate fechaDevolucion;

}
