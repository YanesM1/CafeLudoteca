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
public class Prestamo {
    private Integer idPrestamo;
    private LocalDate fechaSalida;
    private String estadoComponentes;
    private String tipoPrestamo;
    private LocalDate fechaDevolucion;
    private String clienteDocumento;
    private String empleadoDocumento;
   

     @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Prestamo prestamo = (Prestamo) o;

        return idPrestamo == prestamo.idPrestamo
                && Objects.equals(empleadoDocumento, prestamo.empleadoDocumento)
                && Objects.equals(clienteDocumento, prestamo.clienteDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPrestamo, empleadoDocumento, clienteDocumento);
    }


}
