
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
public class Mesa {

    private String idMesa; // PK
    private int capacidad;
    private String tematica;
    private String ubicacion;

    // La igualdad se basa en la llave primaria (idMesa)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mesa mesa = (Mesa) o;
        return Objects.equals(idMesa, mesa.idMesa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMesa);
    }

    @Override
    public String toString() {
        return "Mesa{" +
                "idMesa='" + idMesa + '\'' +
                ", capacidad=" + capacidad +
                ", tematica='" + tematica + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}
