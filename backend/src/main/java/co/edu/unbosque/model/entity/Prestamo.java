package co.edu.unbosque.model.entity;

import java.time.LocalDate;
import java.util.Objects;
import co.edu.unbosque.model.dto.UnidadPrestadaDTO;
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
    /** Todas las unidades del préstamo (supuesto 3: uno o varios juegos físicos). */
    private java.util.List<UnidadPrestadaDTO> unidades = new java.util.ArrayList<>();

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
