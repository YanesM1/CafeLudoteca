package co.edu.unbosque.model.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JuegoMesa {
    private String numSerie;
    private String reglamento;
    private String cantidadMinimaJugador;
    private String cantidadMaximaJugador;
    private int edadMinima;
    private String nombre;
    private int duracionPartida;
    
}
