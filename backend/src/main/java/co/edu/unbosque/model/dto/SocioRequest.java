package dto;

import java.time.LocalDate;

public class SocioRequest {

    private String documento;
    private LocalDate fechaAfiliacion;

    public SocioRequest() {
    }

    public SocioRequest(String documento, LocalDate fechaAfiliacion) {
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
}