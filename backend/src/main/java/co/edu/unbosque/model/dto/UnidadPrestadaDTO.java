package co.edu.unbosque.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Una unidad física incluida en un préstamo (fila de PRESTAMO_UNIDAD_JUEGO). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnidadPrestadaDTO {
    private String codigo;
    private String numSerie;
}
