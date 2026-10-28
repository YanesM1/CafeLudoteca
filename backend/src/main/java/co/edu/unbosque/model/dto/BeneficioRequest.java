package dto;

public class BeneficioRequest {

    private String idMembresia;
    private String idBeneficio;
    private String beneficio;

    public BeneficioRequest() {
    }

    public BeneficioRequest(String idMembresia, String idBeneficio, String beneficio) {
        this.idMembresia = idMembresia;
        this.idBeneficio = idBeneficio;
        this.beneficio = beneficio;
    }

    public String getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(String idMembresia) {
        this.idMembresia = idMembresia;
    }

    public String getIdBeneficio() {
        return idBeneficio;
    }

    public void setIdBeneficio(String idBeneficio) {
        this.idBeneficio = idBeneficio;
    }

    public String getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(String beneficio) {
        this.beneficio = beneficio;
    }
}