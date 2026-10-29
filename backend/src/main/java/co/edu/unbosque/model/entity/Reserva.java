
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
public class Reserva {

    private String idReserva; // PK
    private LocalDate fecha;
    private String horario;
    private String documento; // FK -> Cliente.documento
    private String idMesa; // FK -> Mesa.idMesa

    // La igualdad se basa en la llave primaria (idReserva)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reserva reserva = (Reserva) o;
        return Objects.equals(idReserva, reserva.idReserva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva);
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva='" + idReserva + '\'' +
                ", fecha=" + fecha +
                ", horario='" + horario + '\'' +
                ", documento='" + documento + '\'' +
                ", idMesa='" + idMesa + '\'' +
                '}';
    }
}
