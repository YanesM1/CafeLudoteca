import java.time.LocalDate;
import java.util.Objects;

public class Socio {

    // PK y FK (referencia a Cliente.documento)
    private String documento;
    private LocalDate fechaAfiliacion;

    public Socio() {
    }

    public Socio(String documento, LocalDate fechaAfiliacion) {
        this.documento = documento;
        this.fechaAfiliacion = fechaAfiliacion;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDate getFechaAfiliacion() {
        return fechaAfiliacion;
    }

    public void setFechaAfiliacion(LocalDate fechaAfiliacion) {
        this.fechaAfiliacion = fechaAfiliacion;
    }

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