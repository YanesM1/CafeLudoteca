import java.util.Objects;

public class ClientePrestamo {

    // PK
    private String idPrestamo;
    // FK (referencia a Cliente.documento)
    private String documento;

    public ClientePrestamo() {
    }

    public ClientePrestamo(String idPrestamo, String documento) {
        this.idPrestamo = idPrestamo;
        this.documento = documento;
    }

    public String getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(String idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    // La igualdad se basa en la llave primaria (idPrestamo)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientePrestamo that = (ClientePrestamo) o;
        return Objects.equals(idPrestamo, that.idPrestamo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPrestamo);
    }

    @Override
    public String toString() {
        return "ClientePrestamo{" +
                "idPrestamo='" + idPrestamo + '\'' +
                ", documento='" + documento + '\'' +
                '}';
    }
}