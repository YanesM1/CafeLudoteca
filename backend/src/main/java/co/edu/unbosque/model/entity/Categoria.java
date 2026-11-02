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
public class Categoria {

    private Integer numCategoria;
    private String categoria;
    private String numSerie;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Categoria categoriaObj = (Categoria) o;

        return Objects.equals(numCategoria, categoriaObj.numCategoria)
                && Objects.equals(numSerie, categoriaObj.numSerie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCategoria, numSerie);
    }
}