package dto;

public class ClientePrestamoRequest {

    private String idPrestamo;
    private String documento;

    public ClientePrestamoRequest() {
    }

    public ClientePrestamoRequest(String idPrestamo, String documento) {
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
}