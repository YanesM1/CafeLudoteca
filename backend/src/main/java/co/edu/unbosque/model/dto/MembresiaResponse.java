package dto;

import java.time.LocalDate;

public class MembresiaResponse {

    private String idMembresia;
    private String plan;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String documento;
    private double tarifa;

    public MembresiaResponse() {
    }

    public MembresiaResponse(String idMembresia, String plan, String estado, LocalDate fechaInicio,
                             LocalDate fechaFin, String documento, double tarifa) {
        this.idMembresia = idMembresia;
        this.plan = plan;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.documento = documento;
        this.tarifa = tarifa;
    }

    public String getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(String idMembresia) {
        this.idMembresia = idMembresia;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }
}