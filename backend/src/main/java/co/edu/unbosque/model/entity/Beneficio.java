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

    // Igualdad basada en la llave primaria (idBeneficio) y la FK (idMembresia)
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Beneficio that = (Beneficio) o;

        return Objects.equals(idBeneficio, that.idBeneficio)
                && Objects.equals(idMembresia, that.idMembresia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBeneficio, idMembresia);
    }
}