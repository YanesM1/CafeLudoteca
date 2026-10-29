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
    private int cantidadMinimaJugador;
    private int cantidadMaximaJugador;
    private int edadMinima;
    private String nombre;
    private int duracionPartida;

}
