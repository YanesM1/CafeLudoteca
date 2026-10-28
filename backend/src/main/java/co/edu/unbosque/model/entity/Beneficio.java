import java.util.Objects;

public class Beneficio {

  
    private String idMembresia; // PK FK
    private String idBeneficio; // PK
    private String beneficio;

    public Beneficio() {
    }

    public Beneficio(String idMembresia, String idBeneficio, String beneficio) {
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

    // La igualdad se basa en la llave primaria compuesta (idMembresia + idBeneficio)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beneficio that = (Beneficio) o;
        return Objects.equals(idMembresia, that.idMembresia)
                && Objects.equals(idBeneficio, that.idBeneficio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMembresia, idBeneficio);
    }

    @Override
    public String toString() {
        return "Beneficio{" +
                "idMembresia='" + idMembresia + '\'' +
                ", idBeneficio='" + idBeneficio + '\'' +
                ", beneficio='" + beneficio + '\'' +
                '}';
    }
}