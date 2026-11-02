package co.edu.unbosque.model.entity;
import java.util.Objects;
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
    private Integer cantidadMinimaJugadores;
    private Integer cantidadMaximaJugadores;
    private Integer edadMinima;
    private String nombre;
    private Integer duracionPartida;


    

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    JuegoMesa juegoMesa = (JuegoMesa) o;

    return Objects.equals(numSerie, juegoMesa.numSerie);
}

@Override
public int hashCode() {
    return Objects.hash(numSerie);
}
    
}
