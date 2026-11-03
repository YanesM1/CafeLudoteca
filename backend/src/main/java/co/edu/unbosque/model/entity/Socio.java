package co.edu.unbosque.model.entity;

import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Socio extends  Cliente{
    private LocalDate fechaAfiliacion;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
             return false;

        return Objects.equals(this.getDocumentoCliente(), getDocumentoCliente());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDocumentoCliente());
    }

}