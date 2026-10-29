
package co.edu.unbosque.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoResponseDTO {

    private String documento;
    private String nombre;
    private String apellido;
    private String correo;
    private String cargo;

}
