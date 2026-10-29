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
public class Beneficio {
    private Integer idBeneficio;
    private String beneficio;
    private Integer idMembresia;
  
// La igualdad se basa en la llave primaria compuesta (idMembresia + idBeneficio)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beneficio that = (Beneficio) o;
        return Objects.equals(idMembresia, that.idMembresia)
                && Objects.equals(idBeneficio, that.idBeneficio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMembresia, idBeneficio);
    }

    @Override
public String toString() {
    return "Beneficio{" +
            "idBeneficio=" + idBeneficio +
            ", beneficio='" + beneficio + '\'' +
            ", idMembresia=" + idMembresia +
            '}';
}

}