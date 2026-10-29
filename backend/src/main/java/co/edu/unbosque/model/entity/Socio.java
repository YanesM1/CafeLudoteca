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
public class Socio {

    // PK y FK (referencia a Cliente.documento)
    private String documento;
    private LocalDate fechaAfiliacion;

// La igualdad se basa en la llave primaria (documento)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socio socio = (Socio) o;
        return Objects.equals(documento, socio.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento);
    }

    @Override
    public String toString() {
        return "Socio{" +
                "documento='" + documento + '\'' +
                ", fechaAfiliacion=" + fechaAfiliacion +
                '}';
    }

}