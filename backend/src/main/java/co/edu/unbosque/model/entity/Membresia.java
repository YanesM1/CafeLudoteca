package co.edu.unbosque.model.entity;

import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Membresia {
    private int idMembresia; // PK
    private String plan;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String documento;
    private double tarifa;

    // La igualdad se basa en la llave primaria (idMembresia)
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Membresia that = (Membresia) o;
        return Objects.equals(idMembresia, that.idMembresia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMembresia);
    }
}
