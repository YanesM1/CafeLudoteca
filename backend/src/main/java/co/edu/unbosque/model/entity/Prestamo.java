package co.edu.unbosque.model.entity;

import java.time.LocalDate;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {
    private Integer idPrestamo;
    private LocalDate fechaSalida;
    private String estadoComponentes;
    private String tipoPrestamo;
    private LocalDate fechaDevolucion;
    private String documentoCliente;
    private String documentoEmp;
    private String codigoUnidad;
    private String numSerieUnidad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prestamo p = (Prestamo) o;
        return Objects.equals(idPrestamo, p.idPrestamo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPrestamo);
    }
}
