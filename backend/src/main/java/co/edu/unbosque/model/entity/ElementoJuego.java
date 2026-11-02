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
public class ElementoJuego {
    private Integer idElementoJuego;
    private String elementoJuego;
    private String numSerie;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ElementoJuego elemento = (ElementoJuego) o;

        return idElementoJuego == elemento.idElementoJuego
                && Objects.equals(elementoJuego, elemento.elementoJuego)
                && Objects.equals(numSerie, elemento.numSerie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idElementoJuego, elementoJuego, numSerie);
    }

}
