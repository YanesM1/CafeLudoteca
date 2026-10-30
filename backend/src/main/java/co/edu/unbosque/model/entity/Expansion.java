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
public class Expansion {
    private int idExpansion;
    private String nombre;
    private String descripcion;
    private String contenidoAdicional;
    

    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Expansion expansion = (Expansion) o;

    return idExpansion == expansion.idExpansion;
}

@Override
public int hashCode() {
    return Objects.hash(idExpansion);
}


}
