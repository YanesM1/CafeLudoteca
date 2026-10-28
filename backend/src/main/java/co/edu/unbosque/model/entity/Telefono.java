package model;

import java.util.Objects;

public class Telefono {

    
    private String documento; // PK FK
    private String telefono; // PK

    public Telefono() {
    }

    public Telefono(String documento, String telefono) {
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

    // La igualdad se basa en la llave primaria compuesta (documento + telefono)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telefono that = (Telefono) o;
        return Objects.equals(documento, that.documento)
                && Objects.equals(telefono, that.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento, telefono);
    }

    @Override
    public String toString() {
        return "Telefono{" +
                "documento='" + documento + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}