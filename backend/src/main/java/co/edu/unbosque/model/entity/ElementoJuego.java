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
    private int idElementoJuego;
    private String elementoJuego;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ElementoJuego elementoJuego = (ElementoJuego) o;

        return idElementoJuego == elementoJuego.idElementoJuego;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idElementoJuego);
    }

}
