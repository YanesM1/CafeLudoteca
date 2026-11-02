package co.edu.unbosque.model.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JuegoMesaResponseDTO {
    private String numSerie;
    private String reglamento;
    private Integer cantidadMinimaJugadores;
    private Integer cantidadMaximaJugadores;
    private Integer edadMinima;
    private String nombre;
    private Integer duracionPartida;

}
