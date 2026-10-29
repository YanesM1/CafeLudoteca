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
public class MembresiaResponseDTO {
    private String idMembresia;
    private String plan;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String documento;
    private double tarifa;

}
