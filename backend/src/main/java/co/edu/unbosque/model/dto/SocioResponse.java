package dto;

import java.time.LocalDate;

public class SocioResponse {

    private String documento;
    private LocalDate fechaAfiliacion;

    public SocioResponse() {
    }

    public SocioResponse(String documento, LocalDate fechaAfiliacion) {
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