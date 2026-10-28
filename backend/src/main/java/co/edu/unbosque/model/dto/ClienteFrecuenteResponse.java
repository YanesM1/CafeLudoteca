package dto;

public class ClienteFrecuenteResponse {

    private String documento;
    private int puntosAcumulados;
    private String nivelFidelidad;

    public ClienteFrecuenteResponse() {
    }

    public ClienteFrecuenteResponse(String documento, int puntosAcumulados, String nivelFidelidad) {
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
}