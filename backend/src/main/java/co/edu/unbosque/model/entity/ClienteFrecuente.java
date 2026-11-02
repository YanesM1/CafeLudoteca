package co.edu.unbosque.model.entity;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteFrecuente extends Cliente{
    private int puntosAcumulados;
    private String nivelFidelidad;

// La igualdad se basa en la llave primaria (documento)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClienteFrecuente that = (ClienteFrecuente) o;
        return Objects.equals(this.getDocumento(), that.getDocumento());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDocumento());
    }

   

}