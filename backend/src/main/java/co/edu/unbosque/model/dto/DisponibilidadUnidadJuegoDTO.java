package co.edu.unbosque.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadUnidadJuegoDTO {
    private String numSerie;
    private Integer unidadesDisponibles;
}
