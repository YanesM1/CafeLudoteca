package dto;

public class TelefonoRequest {

    private String documento;
    private String telefono;

    public TelefonoRequest() {
    }

    public TelefonoRequest(String documento, String telefono) {
        this.documento = documento;
        this.telefono = telefono;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}