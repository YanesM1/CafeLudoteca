
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
public class Empleado {

    private String documentoEmp; // PK
    private String cargo;
    private String nombre;
    private String apellido;
    private String correo;
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Empleado empleado = (Empleado) o;
        return Objects.equals(documentoEmp, empleado.documentoEmp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentoEmp);
    }

   
}
