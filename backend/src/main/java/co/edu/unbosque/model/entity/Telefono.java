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
public class Telefono {

    private String documento; // PK FK
    private String telefono; // PK

    // La igualdad se basa en la llave primaria compuesta (documento + telefono)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telefono that = (Telefono) o;
        return Objects.equals(documento, that.documento)
                && Objects.equals(telefono, that.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento, telefono);
    }

    @Override
    public String toString() {
        return "Telefono{" +
                "documento='" + documento + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

}