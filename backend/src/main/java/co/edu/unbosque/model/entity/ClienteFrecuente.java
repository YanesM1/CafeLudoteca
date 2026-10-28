import java.util.Objects;

public class ClienteFrecuente {

    
    private String documento; // PK FK
    private int puntosAcumulados;
    private String nivelFidelidad;

    public ClienteFrecuente() {
    }

    public ClienteFrecuente(String documento, int puntosAcumulados, String nivelFidelidad) {
        this.documento = documento;
        this.puntosAcumulados = puntosAcumulados;
        this.nivelFidelidad = nivelFidelidad;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public void setPuntosAcumulados(int puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }

    public String getNivelFidelidad() {
        return nivelFidelidad;
    }

    public void setNivelFidelidad(String nivelFidelidad) {
        this.nivelFidelidad = nivelFidelidad;
    }

    // La igualdad se basa en la llave primaria (documento)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClienteFrecuente that = (ClienteFrecuente) o;
        return Objects.equals(documento, that.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento);
    }

    @Override
    public String toString() {
        return "ClienteFrecuente{" +
                "documento='" + documento + '\'' +
                ", puntosAcumulados=" + puntosAcumulados +
                ", nivelFidelidad='" + nivelFidelidad + '\'' +
                '}';
    }
}