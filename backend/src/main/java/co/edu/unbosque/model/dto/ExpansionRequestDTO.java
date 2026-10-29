package co.edu.unbosque.model.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpansionRequestDTO {
    private int idExpansion;
    private String nombre;
    private String descripcion;
    private String contenidoAdicional;

}
